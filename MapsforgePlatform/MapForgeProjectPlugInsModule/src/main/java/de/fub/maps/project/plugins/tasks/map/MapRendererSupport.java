/*
 * Copyright (C) 2013 Serdar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fub.maps.project.plugins.tasks.map;

import de.fub.maps.project.detector.model.xmls.ProcessDescriptor;
import de.fub.maps.project.detector.model.xmls.Property;
import de.fub.maps.project.models.Aggregator;
import static de.fub.maps.project.plugins.tasks.map.MapRenderer.PROP_NAME_AGGREGATOR_FILE_PATH;
import de.fub.maps.project.utils.AggregatorUtils;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.modules.OnStop;
import org.openide.util.Exceptions;

/**
 * A helper class that is responsible for the creation of Aggregator template
 * objects and temp files.
 *
 * @author Serdar
 */
final class MapRendererSupport {

    private final MapRenderer mapRenderer;
    private FileObject aggregatorFileObject;

    public MapRendererSupport(MapRenderer mapRenderer) {
        this.mapRenderer = mapRenderer;
    }

    Aggregator createAggregator() throws FileNotFoundException {
        ProcessDescriptor processDescriptor = mapRenderer.getProcessDescriptor();
        if (processDescriptor != null) {
            for (Property propery : processDescriptor.getProperties().getPropertyList()) {
                if (PROP_NAME_AGGREGATOR_FILE_PATH.equalsIgnoreCase(propery.getId())) {
                    String pathString = propery.getValue();
                    if (pathString != null) {
                        FileObject aggregatorFile = FileUtil.getConfigFile(pathString);
                        if (aggregatorFile.isValid()) {
                            File createAggregatorCopy = createAggregatorCopy(aggregatorFile);
                            if (createAggregatorCopy != null) {
                                aggregatorFileObject = FileUtil.toFileObject(createAggregatorCopy);

                                if (aggregatorFileObject != null) {
                                    return AggregatorUtils.createAggregator(aggregatorFileObject);
                                }
                            } else {
                                propery.setValue(null);
                                throw new FileNotFoundException(MessageFormat.format("aggregator {0} does not exist!", aggregatorFile.getPath()));
                            }
                        } else {
                            propery.setValue(null);
                            throw new FileNotFoundException(MessageFormat.format("aggregator {0} does not exist!", aggregatorFile.getPath()));
                        }
                    }
                }
            }
        }
        return null;
    }

    private File createAggregatorCopy(FileObject fileObject) {
        File copyFileObject = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            copyFileObject = File.createTempFile("tmp", ".agg");
            copyFileObject.deleteOnExit();
            inputStream = fileObject.getInputStream();
            outputStream = new FileOutputStream(copyFileObject);
            FileUtil.copy(inputStream, outputStream);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
        return copyFileObject;
    }

    File createTmpfile(FileObject parentFolder) throws IOException {
        File tmpFile = File.createTempFile("tmp", ".gpx", FileUtil.toFile(parentFolder));
        tmpFile.deleteOnExit();
        return tmpFile;
    }

    FileObject createTempFolder(String name) throws IOException {
        String tmpdir = System.getProperty("java.io.tmpdir");
        if (tmpdir != null) {
            File tmpdirFile = new File(tmpdir);
            if (tmpdirFile.exists()) {
                FileObject fileObject = FileUtil.toFileObject(tmpdirFile);
                if (fileObject.getFileObject(hashCode() + name) == null) {
                    return fileObject.createFolder(hashCode() + name);
                } else {
                    return fileObject.getFileObject(hashCode() + name);
                }
            }
        }
        throw new FileNotFoundException("Couldn't find temp dir!"); // NO18N
    }

    @OnStop
    public static class TempFolderCleaner implements Runnable {

        @Override
        public void run() {
            String tmpdir = System.getProperty("java.io.tmpdir");
            if (tmpdir != null) {
                File tmpFile = new File(tmpdir);
                if (tmpFile.exists()) {
                    for (File file : tmpFile.listFiles(new FileFilterImpl())) {
                        file.delete();
                    }
                }
            }
        }

        private static class FileFilterImpl implements FileFilter {

            public FileFilterImpl() {
            }

            @Override
            public boolean accept(File pathname) {
                return (pathname.isFile() && pathname.getAbsolutePath().endsWith(".agg"))
                        || (pathname.isDirectory() && pathname.getAbsolutePath().matches("MapRendererTransportation"));
            }
        }
    }
}
