/*
 * Copyright 2013 Serdar.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.fub.maps.project.openstreetmap.ui.controller;

import de.fub.maps.project.datasource.service.DataImportService;
import de.fub.maps.project.openstreetmap.service.LocationBoundingBoxService.BoundingBox;
import de.fub.maps.project.openstreetmap.service.OpenstreetMapService;
import de.fub.maps.project.openstreetmap.ui.MapViewerBoundingBoxProvider;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.xml.bind.JAXBException;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.Task;
import org.openide.util.TaskListener;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Serdar
 */
@NbBundle.Messages({"CLT_Name=OSM Import..."})
@ServiceProvider(service = DataImportService.class)
public class OsmGpxImportServiceProvider implements DataImportService, TaskListener {

    private static final Logger LOG = Logger.getLogger(OsmGpxImportServiceProvider.class.getName());
    private FileObject destFolder;
    private final Object MUTEX = new Object();
    private final MapViewerBoundingBoxProvider view = new MapViewerBoundingBoxProvider();
    private final RequestProcessor requestProcessor = new RequestProcessor(getClass().getName(), Runtime.getRuntime().availableProcessors() * 4);
    private int workUnits;
    private ProgressHandle handler;
    private int workUnit;
    private Date timestamp = null;

    public OsmGpxImportServiceProvider() {
        view.getDownloadButton().addActionListener(OsmGpxImportServiceProvider.this);
    }

    @Override
    public void setDestinationFolder(FileObject destFolder) {
        assert destFolder != null;
        this.destFolder = destFolder;
    }

    public FileObject getDestFolder() {
        return destFolder;
    }

    @NbBundle.Messages({"CLT_OSM_GPX_Import=OSM GPX File Importer"})
    @Override
    public void actionPerformed(ActionEvent e) {
        synchronized (MUTEX) {
            if (e != null) {
                if (view.getDownloadButton().equals(e.getSource())) {
                    startDownLoadProcess();
                } else {
                    DialogDescriptor dd = new DialogDescriptor(view, Bundle.CLT_OSM_GPX_Import(), true, null);
                    DialogDisplayer.getDefault().notifyLater(dd);
                }
            }
        }
    }

    public Date getTimestamp() {
        return new Date(timestamp.getTime());
    }

    @NbBundle.Messages({
        "# {0} - start",
        "# {1} - end",
        "Fetching_GPX_Data=Downloading data {0} to {1}"})
    private void startDownLoadProcess() {
        timestamp = Calendar.getInstance().getTime();
        view.lockInputFields(true);
        int start = (Integer) view.getPageStart().getValue();
        int end = (Integer) view.getPageEnd().getValue();
        handler = ProgressHandleFactory.createHandle(Bundle.Fetching_GPX_Data(start, end));
        view.setProgressBar(ProgressHandleFactory.createProgressComponent(handler));
        workUnits = end - start;
        workUnit = 0;

        try {
            handler.start();
            handler.switchToDeterminate(workUnits);
            for (; start <= end; start++) {
                FetchJob fetchJob = new FetchJob(OsmGpxImportServiceProvider.this, start);
                RequestProcessor.Task task = requestProcessor.create(fetchJob);
                task.addTaskListener(OsmGpxImportServiceProvider.this);
                task.schedule(0);
            }
        } catch (Exception ex) {
            handler.finish();
            view.setProgressBar(null);
            view.lockInputFields(false);
        }
    }

    @Override
    public void taskFinished(Task task) {
        synchronized (MUTEX) {

            if (handler != null) {
                handler.progress(workUnit);
                if (workUnit >= workUnits) {
                    handler.finish();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            view.lockInputFields(false);
                            view.setProgressBar(null);
                        }
                    });
                }
            }
            workUnit++;
            LOG.log(Level.INFO, "finished workunit {0}", workUnit);
        }
    }

    @Override
    public String getName() {
        return Bundle.CLT_Name();
    }

    private static class FetchJob implements Runnable {

        private final OsmGpxImportServiceProvider provider;
        private final BoundingBox boundingBox;
        private final int page;

        public FetchJob(OsmGpxImportServiceProvider provider, int page) {
            this.provider = provider;
            this.boundingBox = provider.view.getViewBoundingBox();
            this.page = page;
        }

        private FileObject getGPXFolder() throws IOException {
            String bboxFolderName = MessageFormat.format("[{4}]_{0, number, integer}_{1}_{2}_{3}",
                    boundingBox.getLeftLongitude(),
                    boundingBox.getBottomLatitude(),
                    boundingBox.getRightLongitude(),
                    boundingBox.getTopLatitude(),
                    new SimpleDateFormat("dd.MM.yyyy HH.mm.ss.SSS").format(provider.getTimestamp()));
            FileObject gpxFileFolder = provider.getDestFolder().getFileObject(bboxFolderName);
            if (gpxFileFolder == null) {
                gpxFileFolder = provider.getDestFolder().createFolder(bboxFolderName);
            }
            return gpxFileFolder;
        }

        private FileObject getGPXFile(FileObject gpxFileFolder) throws IOException {
            String gpxFileName = MessageFormat.format("Gpx_{0, number, 00000}.gpx", page);
            FileObject gpxFileObject = gpxFileFolder.getFileObject(gpxFileName);
            if (gpxFileObject == null) {
                gpxFileObject = gpxFileFolder.createData(gpxFileName);
            }
            return gpxFileObject;
        }

        private void writeGPX(de.fub.maps.project.openstreetmap.xml.gpx.Gpx gpx) throws IOException, JAXBException {
            OutputStream outputStream = null;
            try {
                if (gpx != null && gpx.getTrk() != null && !gpx.getTrk().isEmpty()) {
                    FileObject gpxFileObject = getGPXFile(getGPXFolder());
                    outputStream = gpxFileObject.getOutputStream();
                    javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(de.fub.maps.project.openstreetmap.xml.gpx.Gpx.class);
                    javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
                    marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
                    marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                    marshaller.marshal(gpx, outputStream);
                }
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
        }

        @Override
        public void run() {
            OpenstreetMapService openstreetMapService = null;

            try {
                openstreetMapService = new OpenstreetMapService();
                de.fub.maps.project.openstreetmap.xml.gpx.Gpx gpx = openstreetMapService.getGpsTracks(
                        de.fub.maps.project.openstreetmap.xml.gpx.Gpx.class,
                        String.valueOf(boundingBox.getLeftLongitude()),
                        String.valueOf(boundingBox.getBottomLatitude()),
                        String.valueOf(boundingBox.getRightLongitude()),
                        String.valueOf(boundingBox.getTopLatitude()),
                        String.valueOf(page));

                writeGPX(gpx);
                LOG.log(Level.INFO, "finished with page {0}", page);
            } catch (IOException ex) {
                LOG.log(Level.INFO, ex.getMessage(), ex);
            } catch (JAXBException ex) {
                LOG.log(Level.INFO, ex.getMessage(), ex);
            } finally {
                if (openstreetMapService != null) {
                    openstreetMapService.close();
                }
            }
        }
    }
}
