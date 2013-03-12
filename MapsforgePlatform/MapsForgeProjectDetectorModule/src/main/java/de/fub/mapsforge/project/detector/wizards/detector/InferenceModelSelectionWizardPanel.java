/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.mapsforge.project.detector.wizards.detector;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.explorer.ExplorerManager;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;
import org.openide.util.WeakListeners;

public class InferenceModelSelectionWizardPanel implements WizardDescriptor.FinishablePanel<WizardDescriptor>, PropertyChangeListener {

    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private InferenceModelSelectionVisualPanel component;
    private final ChangeSupport cs = new ChangeSupport(this);
    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.

    @Override
    public InferenceModelSelectionVisualPanel getComponent() {
        if (component == null) {
            component = new InferenceModelSelectionVisualPanel();
            component.getExplorerManager().addPropertyChangeListener(WeakListeners.propertyChange(InferenceModelSelectionWizardPanel.this, component.getExplorerManager()));
        }
        return component;
    }

    @Override
    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
        // If you have context help:
        // return new HelpCtx("help.key.here");
    }

    @Override
    public boolean isValid() {
        // If it is always OK to press Next or Finish, then:
        return getComponent().getExplorerManager().getSelectedNodes().length == 1;
        // If it depends on some condition (form filled out...) and
        // this condition changes (last form field filled in...) then
        // use ChangeSupport to implement add/removeChangeListener below.
        // WizardDescriptor.ERROR/WARNING/INFORMATION_MESSAGE will also be useful.
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        cs.addChangeListener(l);
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        cs.removeChangeListener(l);
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
        // use wiz.getProperty to retrieve previous panel state
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
        wiz.putProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, 5);
        // use wiz.putProperty to remember current panel state
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (ExplorerManager.PROP_SELECTED_NODES.equals(evt.getPropertyName())) {
            cs.fireChange();
        }
    }

    @Override
    public boolean isFinishPanel() {
        return isValid();
    }
}