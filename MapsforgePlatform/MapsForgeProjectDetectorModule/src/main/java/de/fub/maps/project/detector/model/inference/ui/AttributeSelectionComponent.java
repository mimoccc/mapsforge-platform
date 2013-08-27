/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.maps.project.detector.model.inference.ui;

import de.fub.maps.project.detector.DetectorMode;
import de.fub.maps.project.detector.model.Detector;
import de.fub.maps.project.detector.model.gpx.TrackSegment;
import de.fub.maps.project.detector.model.inference.AbstractInferenceModel;
import de.fub.maps.project.detector.model.inference.features.FeatureProcess;
import de.fub.maps.project.detector.model.pipeline.preprocessors.FilterProcess;
import de.fub.maps.project.detector.model.xmls.Profile;
import java.awt.Image;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.JToolBar;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.BeanNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.Task;
import org.openide.util.TaskListener;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Serdar
 */
@NbBundle.Messages({
    "CLT_AttributeSelectionComponent_handle_Name=Running attribute selection...",
    "# {0} - name",
    "CLT_AttributeSelectionComponent_DisplayName={0} [Attribute Selection]"
})
public class AttributeSelectionComponent extends TopComponent implements TaskListener {

    private static final long serialVersionUID = 1L;
    private Detector detector;
    private AttributeSelectionTask attributeSelectionTask;
    private ProgressHandle handle;
    private final JToolBar toolBar = new JToolBar();
    private static final RequestProcessor REQUESTPROCESSER = new RequestProcessor(AttributeSelectionComponent.class.getName());

    /**
     * Creates new form AttributeSelectionComponent
     */
    public AttributeSelectionComponent() {
        initComponents();
        attributeRankingOutlineView.getOutline().setRootVisible(false);
        attributeSelectionOutlineView.getOutline().setRootVisible(false);
        attributeSelectionBarChart1.getBarChart().setTitle((TextTitle) null);
    }

    public AttributeSelectionComponent(Detector detector) {
        this();
        this.detector = detector;
        this.attributeSelectionTask = new AttributeSelectionTask(detector);
        Node nodeDelegate = this.detector.getDataObject().getNodeDelegate();
        setDisplayName(Bundle.CLT_AttributeSelectionComponent_DisplayName(nodeDelegate.getDisplayName()));
        setActivatedNodes(new Node[]{nodeDelegate});
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        attributeSelectionContentPanel = new de.fub.utilsmodule.components.ExplorerManagerProviderPanel();
        attributeSelectionOutlineView = new org.openide.explorer.view.OutlineView(NbBundle.getMessage(AttributeSelectionComponent.class, "AttributeSelectionComponent.attributeSelectionOutlineView.node.name"));
        jLabel2 = new javax.swing.JLabel();
        attributeRankingContentPanel = new de.fub.utilsmodule.components.ExplorerManagerProviderPanel();
        attributeRankingOutlineView = new org.openide.explorer.view.OutlineView(NbBundle.getMessage(AttributeSelectionComponent.class, "AttributeSelectionComponent.attributeRankingOutlineView.node.name"));
        jLabel1 = new javax.swing.JLabel();
        attributeRankingChartContentPanel = new javax.swing.JPanel();
        attributeSelectionBarChart1 = new de.fub.maps.project.detector.model.inference.ui.charts.AttributeSelectionBarChart();
        jLabel3 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();

        setPreferredSize(new java.awt.Dimension(550, 500));
        setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.GridLayout(3, 1, 0, 2));

        attributeSelectionContentPanel.setBackground(new java.awt.Color(255, 216, 178));
        attributeSelectionContentPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        attributeSelectionContentPanel.setLayout(new java.awt.BorderLayout());

        attributeSelectionOutlineView.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        attributeSelectionContentPanel.add(attributeSelectionOutlineView, java.awt.BorderLayout.CENTER);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(AttributeSelectionComponent.class, "AttributeSelectionComponent.jLabel2.text")); // NOI18N
        jLabel2.setPreferredSize(new java.awt.Dimension(152, 24));
        attributeSelectionContentPanel.add(jLabel2, java.awt.BorderLayout.PAGE_START);

        jPanel4.add(attributeSelectionContentPanel);

        attributeRankingContentPanel.setBackground(new java.awt.Color(255, 216, 178));
        attributeRankingContentPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        attributeRankingContentPanel.setLayout(new java.awt.BorderLayout());

        attributeRankingOutlineView.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        attributeRankingOutlineView.setPropertyColumns(new String[] {"index", "Index", "merit", "Merit (%)"});
        attributeRankingContentPanel.add(attributeRankingOutlineView, java.awt.BorderLayout.CENTER);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(AttributeSelectionComponent.class, "AttributeSelectionComponent.jLabel1.text")); // NOI18N
        jLabel1.setPreferredSize(new java.awt.Dimension(34, 24));
        attributeRankingContentPanel.add(jLabel1, java.awt.BorderLayout.PAGE_START);

        jPanel4.add(attributeRankingContentPanel);

        attributeRankingChartContentPanel.setBackground(new java.awt.Color(255, 216, 178));
        attributeRankingChartContentPanel.setLayout(new java.awt.BorderLayout());

        attributeSelectionBarChart1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        attributeRankingChartContentPanel.add(attributeSelectionBarChart1, java.awt.BorderLayout.CENTER);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(AttributeSelectionComponent.class, "AttributeSelectionComponent.jLabel3.text")); // NOI18N
        jLabel3.setPreferredSize(new java.awt.Dimension(34, 24));
        attributeRankingChartContentPanel.add(jLabel3, java.awt.BorderLayout.PAGE_START);

        jPanel4.add(attributeRankingChartContentPanel);

        jPanel2.add(jPanel4, java.awt.BorderLayout.CENTER);

        add(jPanel2, java.awt.BorderLayout.CENTER);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setPreferredSize(new java.awt.Dimension(13, 22));
        jToolBar1.setRequestFocusEnabled(false);
        add(jToolBar1, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel attributeRankingChartContentPanel;
    private de.fub.utilsmodule.components.ExplorerManagerProviderPanel attributeRankingContentPanel;
    private org.openide.explorer.view.OutlineView attributeRankingOutlineView;
    private de.fub.maps.project.detector.model.inference.ui.charts.AttributeSelectionBarChart attributeSelectionBarChart1;
    private de.fub.utilsmodule.components.ExplorerManagerProviderPanel attributeSelectionContentPanel;
    private org.openide.explorer.view.OutlineView attributeSelectionOutlineView;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void taskFinished(Task task) {
        handle.finish();
        List<AttributeWrapper> rankedAttributeList = attributeSelectionTask.getRankedAttributeList();
        List<AttributeWrapper> selectedAttributeList = attributeSelectionTask.getSelectedAttributeList();
        Collections.sort(rankedAttributeList);

        attributeRankingContentPanel.getExplorerManager().setRootContext(new RootNode(rankedAttributeList));
        attributeSelectionContentPanel.getExplorerManager().setRootContext(new RootNode(selectedAttributeList));

        DefaultCategoryDataset dataset = attributeSelectionBarChart1.getDataset();
        attributeSelectionBarChart1.getBarChart().removeLegend();
        for (AttributeWrapper attribute : rankedAttributeList) {
            dataset.addValue(attribute.getMerit(), "attribute", attribute.getName());
        }
        repaint();

    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    protected void componentOpened() {
        super.componentOpened();
        handle = ProgressHandleFactory.createHandle(Bundle.CLT_AttributeSelectionComponent_handle_Name());
        handle.start();
        RequestProcessor.Task task = REQUESTPROCESSER.create(this.attributeSelectionTask);
        task.addTaskListener(WeakListeners.create(TaskListener.class, AttributeSelectionComponent.this, task));
        task.schedule(0);
    }

    // <editor-fold defaultstate="collapsed" desc="Nodes and Factories">
    private static class RootNode extends AbstractNode {

        private RootNode(List<AttributeWrapper> rankedAttributeList) {
            super(Children.create(new NodeFactory(rankedAttributeList), true));
        }
    }

    private static class NodeFactory extends ChildFactory<AttributeWrapper> {

        private final List<AttributeWrapper> attributeList;

        private NodeFactory(List<AttributeWrapper> rankedAttributeList) {
            this.attributeList = rankedAttributeList;
        }

        @Override
        protected boolean createKeys(List<AttributeWrapper> toPopulate) {
            toPopulate.addAll(attributeList);
            return true;
        }

        @Override
        protected Node createNodeForKey(AttributeWrapper attribute) {
            Node node = Node.EMPTY;
            try {
                node = new AttributeNode(attribute);
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
            return node;
        }
    }

    private static class AttributeNode extends AbstractNode {

        private final AttributeWrapper attribute;
        private final static Node DUMMY_NODE = createBeanNode();

        public AttributeNode(AttributeWrapper attribute) throws IntrospectionException {
            super(Children.LEAF, Lookups.singleton(attribute));
            setDisplayName(attribute.getName());
            this.attribute = attribute;
        }

        @Override
        protected Sheet createSheet() {
            Sheet sheet = Sheet.createDefault();
            Sheet.Set set = Sheet.createPropertiesSet();
            sheet.put(set);


            Property<?> property = new PropertySupport.ReadOnly<Integer>("index", Integer.class, "Index", "Ranke of this attribute") {
                @Override
                public Integer getValue() throws IllegalAccessException, InvocationTargetException {
                    int index = attribute.getIndex();
                    return index;
                }
            };

            set.put(property);

            property = new PropertySupport.ReadOnly<Double>("merit", Double.class, "Merit (%)", "The merit value of this attribute") {
                @Override
                public Double getValue() throws IllegalAccessException, InvocationTargetException {
                    double merit = attribute.getMerit();
                    return merit;
                }
            };
            set.put(property);
            return sheet;
        }

        @Override
        public Image getIcon(int type) {
            return DUMMY_NODE != null ? DUMMY_NODE.getIcon(type) : super.getIcon(type);
        }

        @Override
        public Image getOpenedIcon(int type) {
            return getIcon(type);
        }

        private static Node createBeanNode() {
            Node node = Node.EMPTY;
            try {
                node = new BeanNode<Object>(new Object());
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
            return node;
        }
    }
    // </editor-fold>

    private static class AttributeSelectionTask implements Runnable {

        private static final Logger LOG = Logger.getLogger(AttributeSelectionTask.class.getName());
        private final Detector detector;
        private List<AttributeWrapper> rankedAttributeList = new ArrayList<AttributeWrapper>();
        private List<AttributeWrapper> selectedAttributeList = new ArrayList<AttributeWrapper>();
        private final Profile currentProfile;

        private AttributeSelectionTask(Detector detector) {
            this.detector = detector;
            this.currentProfile = this.detector.getCurrentActiveProfile();
        }

        private AbstractInferenceModel getInferenceModel() {
            return detector.getInferenceModel();
        }

        public List<AttributeWrapper> getRankedAttributeList() {
            return rankedAttributeList;
        }

        public List<AttributeWrapper> getSelectedAttributeList() {
            return selectedAttributeList;
        }

        @Override
        public void run() {
            Instances trainingSet = createTrainingsSet();
            evaluate(trainingSet);
            classEvaluation(trainingSet);
        }

        private Instances createTrainingsSet() {

            Collection<Attribute> attributeList = getInferenceModel().getAttributes();
            Instances trainingSet = new Instances("Classes", new ArrayList<Attribute>(attributeList), 9);
            trainingSet.setClassIndex(0);
            Map<String, List<TrackSegment>> dataset = detector.getTrainingsSet();

            // check whether the current active profile is set to filter the trainings set
            // if so, apply the current filters
            if (this.currentProfile.getPreprocess().isActive()
                    && (this.currentProfile.getPreprocess().getMode() == DetectorMode.TRAINING
                    || this.currentProfile.getPreprocess().getMode() == DetectorMode.BOTH)) {
                final ProgressHandle filterHandle = ProgressHandleFactory.createHandle("Applying Preprocessors...");
                Set<Map.Entry<String, List<TrackSegment>>> entrySet = dataset.entrySet();
                filterHandle.start(entrySet.size());
                int index = 0;
                try {
                    for (Map.Entry<String, List<TrackSegment>> entry : entrySet) {
                        List<TrackSegment> tracks = entry.getValue();
                        // TODO could lead to an infinity loop or to a concurrent modification exception!
                        dataset.put(entry.getKey(), applyPreProcessors(tracks));
                        filterHandle.progress(index++);
                    }
                } finally {
                    filterHandle.finish();
                }
            }

            for (Map.Entry<String, List<TrackSegment>> entry : dataset.entrySet()) {
                for (TrackSegment trackSegment : entry.getValue()) {
                    Instance instance = getInstance(entry.getKey(), trackSegment);
                    trainingSet.add(instance);
                }
            }

            assert trainingSet.numInstances() > 0 : "Training set is empty and has no instances"; //NO18N
            return trainingSet;
        }

        private List<TrackSegment> applyPreProcessors(List<TrackSegment> dataset) {
            List<TrackSegment> gpsTracks = new ArrayList<TrackSegment>();
            List<TrackSegment> tracks = dataset;
            for (FilterProcess filterProcess : detector.getPreProcessorPipeline().getProcesses()) {
                filterProcess.setInput(tracks);
                filterProcess.run();
                tracks = filterProcess.getResult();
            }

            gpsTracks.addAll(tracks);
            return gpsTracks;
        }

        private void classEvaluation(Instances trainingSet) {
            try {
                AttributeSelectedClassifier attributeSelectedClassifier = new AttributeSelectedClassifier();
                CfsSubsetEval eval = new CfsSubsetEval();
                GreedyStepwise search = new GreedyStepwise();
                search.setSearchBackwards(true);
                search.setGenerateRanking(true);
                attributeSelectedClassifier.setClassifier(new J48());
                attributeSelectedClassifier.setEvaluator(eval);
                attributeSelectedClassifier.setSearch(search);
                Evaluation crossEvaluation = new Evaluation(trainingSet);
                crossEvaluation.crossValidateModel(attributeSelectedClassifier, trainingSet, 10, new Random(1));

                int[] selectedAttribute = search.search(eval, crossEvaluation.getHeader());
                double[][] rankedAttributes = search.rankedAttributes();
                LOG.info(Arrays.toString(selectedAttribute));
                for (int i = 0; i < rankedAttributes.length; i++) {
                    LOG.info(Arrays.toString(rankedAttributes[i]));
                }
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        private void evaluate(Instances trainingSet) {
            try {
                AttributeSelection attsel = new AttributeSelection();  // package weka.attributeSelection!
                CfsSubsetEval eval = new CfsSubsetEval();
                GreedyStepwise search = new GreedyStepwise();
                search.setSearchBackwards(true);
                search.setGenerateRanking(true);
                attsel.setRanking(true);
                attsel.setFolds(10);
                attsel.setSeed(1);
                attsel.setEvaluator(eval);
                attsel.setSearch(search);
                attsel.selectAttributesCVSplit(trainingSet);
                LOG.info(attsel.CrossValidateAttributes());
                attsel.SelectAttributes(trainingSet);
                LOG.info(attsel.toResultsString());

                // obtain the attribute indices that were selected
                int[] attributeSelection = attsel.selectedAttributes();
                LOG.info(Arrays.toString(attributeSelection));
                int[] selectedAttributes = search.search(eval, trainingSet);
                double[][] rankedAttributes = attsel.rankedAttributes();
                ArrayList<Attribute> attributeList = new ArrayList<Attribute>(getInferenceModel().getAttributes());
                for (int i = 0; i < selectedAttributes.length; i++) {
                    AttributeWrapper selectedAttribute = new AttributeWrapper();
                    selectedAttributeList.add(selectedAttribute);
                    selectedAttribute.setIndex(i);
                    int attributeIndex = selectedAttributes[i];

                    if (attributeIndex < attributeList.size()) {
                        Attribute attribute = attributeList.get(attributeIndex);
                        LOG.info(attribute.name());
                        selectedAttribute.setName(attribute.name());
                    }
                }

                for (int i = 0; i < rankedAttributes.length; i++) {
                    AttributeWrapper rankedAttribute = new AttributeWrapper();
                    rankedAttributeList.add(rankedAttribute);
                    for (int j = 0; j < rankedAttributes[i].length; j++) {
                        switch (j) {
                            case 0:
                                int attributeIndex = (int) rankedAttributes[i][j];
                                rankedAttribute.setIndex(i + 1);
                                if (attributeIndex < attributeList.size()) {
                                    Attribute attribute = attributeList.get(attributeIndex);
                                    LOG.info(attribute.name());
                                    rankedAttribute.setName(attribute.name());
                                }
                                break;
                            case 1:
                                rankedAttribute.setMerit(Double.parseDouble(MessageFormat.format("{0,number,000.00}", rankedAttributes[i][j] * 100).replaceAll(",", "\\.")));
                                break;
                            default:
                                throw new AssertionError();
                        }
                    }
                }

            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        private Instance getInstance(String className, TrackSegment dataset) {
            Instance instance = new DenseInstance(getInferenceModel().getAttributes().size());

            for (FeatureProcess feature : getInferenceModel().getFeatureList()) {
                feature.setInput(dataset);
                feature.run();
                String featureName = feature.getName();
                Attribute attribute = getInferenceModel().getAttributeMap().get(featureName);
                Double result = feature.getResult();
                instance.setValue(attribute, result);
            }

            instance.setValue(getInferenceModel().getAttributeMap().get(AbstractInferenceModel.CLASSES_ATTRIBUTE_NAME), className);
            return instance;
        }
    }

    private static class AttributeWrapper implements Comparable<AttributeWrapper> {

        private String name;
        private int index;
        private double merit;

        public AttributeWrapper() {
        }

        public AttributeWrapper(String name, int index, double merit) {
            this.name = name;
            this.index = index;
            this.merit = merit;
        }

        public String getName() {
            return name;
        }

        public int getIndex() {
            return index;
        }

        public double getMerit() {
            return merit;
        }

        private void setName(String name) {
            this.name = name;
        }

        private void setIndex(int index) {
            this.index = index;
        }

        private void setMerit(double merit) {
            this.merit = merit;
        }

        @Override
        public int compareTo(AttributeWrapper o) {
            return index == o.index ? 0 : index < o.index ? -1 : 1;
        }
    }
}