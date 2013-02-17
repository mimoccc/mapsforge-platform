/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.mapsforge.project.aggregator.filetype;

import de.fub.mapsforge.project.aggregator.factories.nodes.AggregatorNode;
import de.fub.mapsforge.project.aggregator.xml.AggregatorDescriptor;
import de.fub.mapsforge.project.models.Aggregator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import javax.swing.event.ChangeListener;
import javax.swing.text.StyledDocument;
import javax.xml.bind.JAXBException;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.filesystems.FileChangeAdapter;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.Node;
import org.openide.text.DataEditorSupport;
import org.openide.util.ChangeSupport;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

@Messages({
    "LBL_AggregationBuilder_LOADER=Files of AggregationBuilder"
})
@MIMEResolver.ExtensionRegistration(
        displayName = "#LBL_AggregationBuilder_LOADER",
        mimeType = "text/aggregationbuilder+xml",
        extension = {"agg", "Agg", "AGG"})
@MIMEResolver.NamespaceRegistration(
        displayName = "#LBL_AggregationBuilder_LOADER",
        mimeType = "text/aggregationbuilder+xml",
        elementNS = {"http://inf.fu-berlin.de/mapsforge/aggregation/schema"},
        elementName = "aggregator")
@DataObject.Registration(
        mimeType = "text/aggregationbuilder+xml",
        iconBase = "de/fub/mapsforge/project/aggregator/filetype/aggregationBuilderIcon.png",
        displayName = "#LBL_AggregationBuilder_LOADER",
        position = 300)
@ActionReferences({
    @ActionReference(
            path = "Loaders/text/aggregationbuilder+xml/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
            position = 100,
            separatorAfter = 200),
    @ActionReference(
            path = "Loaders/text/aggregationbuilder+xml/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
            position = 300),
    @ActionReference(
            path = "Loaders/text/aggregationbuilder+xml/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
            position = 400,
            separatorAfter = 500),
    @ActionReference(
            path = "Loaders/text/aggregationbuilder+xml/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
            position = 600),
    @ActionReference(
            path = "Loaders/text/aggregationbuilder+xml/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
            position = 700,
            separatorAfter = 800),
    @ActionReference(
            path = "Loaders/text/aggregationbuilder+xml/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
            position = 900,
            separatorAfter = 1000),
    @ActionReference(
            path = "Loaders/text/aggregationbuilder+xml/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
            position = 1100,
            separatorAfter = 1200),
    @ActionReference(
            path = "Loaders/text/aggregationbuilder+xml/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
            position = 1300),
    @ActionReference(
            path = "Loaders/text/aggregationbuilder+xml/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
            position = 1400)
})
public class AggregatorDataObject extends MultiDataObject {

    private static final long serialVersionUID = 1L;
    private ChangeSupport cs = new ChangeSupport(this);
    private AggregatorNode delegateNode = null;
    private AggregatorDescriptor aggregator = null;
    private FileChangeAdapter fileChangeListener = new FileChangeAdapter() {
        @Override
        public void fileChanged(FileEvent fe) {
            aggregator = null;
            cs.fireChange();
        }
    };

    public AggregatorDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        registerEditor("text/aggregationbuilder+xml", true);

        pf.addFileChangeListener(FileUtil.weakFileChangeListener(fileChangeListener, pf));
        DataEditorSupport editorSupport = getLookup().lookup(DataEditorSupport.class);
    }

    public synchronized AggregatorDescriptor getAggregator() throws IOException, JAXBException {
        if (aggregator == null) {
            InputStream inputStream = null;
            try {
                inputStream = getPrimaryFile().getInputStream();
                javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(AggregatorDescriptor.class);
                javax.xml.bind.Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
                aggregator = (AggregatorDescriptor) unmarshaller.unmarshal(inputStream);
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }
        return aggregator;
    }

    @Override
    protected int associateLookup() {
        return 1;
    }

    public void save() {
        FileUtil.runAtomicAction(new Runnable() {
            @Override
            public void run() {
                try {
                    AggregatorDescriptor oldAggregator = aggregator;
                    aggregator = null;

                    File file = FileUtil.toFile(getPrimaryFile());
                    if (file != null) {
                        javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(AggregatorDescriptor.class);
                        javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
                        marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
                        marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                        marshaller.marshal(oldAggregator, file);
                        FileUtil.refreshFor(file);
                    }
                } catch (JAXBException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });

    }

    public void saveFromEditor() {
        FileUtil.runAtomicAction(new Runnable() {
            @Override
            public void run() {
                AggregatorDescriptor temp = aggregator;
                try {
                    DataEditorSupport editorSupport = getLookup().lookup(DataEditorSupport.class);

                    aggregator = null;


                    javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(AggregatorDescriptor.class);
                    javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
                    marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
                    marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

                    StringWriter stringWriter = new StringWriter();
                    marshaller.marshal(temp, stringWriter);
                    StyledDocument document = editorSupport.getDocument();
                    document.remove(0, document.getLength());
                    document.insertString(0, stringWriter.toString(), null);
                    editorSupport.saveDocument();
                } catch (Exception ex) {
                    aggregator = temp;
                }
            }
        });

    }

    @MultiViewElement.Registration(
            displayName = "#LBL_AggregationBuilder_EDITOR",
            iconBase = "de/fub/mapsforge/project/aggregator/filetype/aggregationBuilderIcon.png",
            mimeType = "text/aggregationbuilder+xml",
            persistenceType = TopComponent.PERSISTENCE_NEVER,
            preferredID = "AggregationBuilder",
            position = 1000)
    @Messages("LBL_AggregationBuilder_EDITOR=Source")
    public static MultiViewEditorElement createEditor(Lookup lkp) {
        return new MultiViewEditorElement(lkp);
    }

    @Override
    protected Node createNodeDelegate() {
        if (delegateNode == null) {
            delegateNode = new AggregatorNode(new Aggregator(AggregatorDataObject.this));
        }
        return delegateNode;
    }

    public void addChangeListener(ChangeListener listener) {
        cs.addChangeListener(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        cs.removeChangeListener(listener);
    }
}