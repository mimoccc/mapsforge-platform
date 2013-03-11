/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.mapsforge.project.detector.wizards.detector;

import javax.swing.JPanel;
import org.openide.explorer.ExplorerManager;
import org.openide.util.NbBundle;

/**
 *
 * @author Serdar
 */
@NbBundle.Messages({
    "CLT_InferenceDataset_Name=Inference Data Set"
})
public class InferenceDataSetSelectionVisualPanel extends JPanel implements ExplorerManager.Provider {

    private static final long serialVersionUID = 1L;
    private final ExplorerManager explorerManager = new ExplorerManager();

    /**
     * Creates new form InferenceDataSetSelectionVisualPanel
     */
    public InferenceDataSetSelectionVisualPanel() {
        initComponents();
        beanTreeView1.setRootVisible(true);
    }

    @Override
    public String getName() {
        return Bundle.CLT_InferenceDataset_Name();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        beanTreeView1 = new org.openide.explorer.view.BeanTreeView();

        setPreferredSize(new java.awt.Dimension(400, 300));
        setLayout(new java.awt.BorderLayout());

        beanTreeView1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        beanTreeView1.setPreferredSize(new java.awt.Dimension(400, 300));
        add(beanTreeView1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.openide.explorer.view.BeanTreeView beanTreeView1;
    // End of variables declaration//GEN-END:variables

    @Override
    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }
}
