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
package de.fub.gpxmodule.actions.ui;

import java.text.MessageFormat;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.DialogDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.util.NbBundle;

/**
 *
 * @author Serdar
 */
@NbBundle.Messages({"CLT_Merge_Dialog_Title=Merge Dialog",
    "CLT_Merge_Dialog_Error_Message_Filename=You have to specify a file name, which does not contain '/' and '\' charaters!",
    "CLT_Merge_Dialog_Error_Message_File_exists=The specified file path already exists! Specify another path."})
public class MergeForm extends javax.swing.JPanel implements DocumentListener {

    private static final long serialVersionUID = 1L;
    private transient DialogDescriptor descriptor;
    private final FileObject parentFolder;

    /**
     * Creates new form MergeForm
     *
     * @param parentFolder
     */
    public MergeForm(FileObject parentFolder) {
        assert parentFolder != null : "parent fileobject can't be null";
        this.parentFolder = parentFolder;
        initComponents();
        init();
    }

    private void init() {
        getDescriptor().createNotificationLineSupport();
        getFilename().getDocument().addDocumentListener(MergeForm.this);
        getFoldername().getDocument().addDocumentListener(MergeForm.this);
        getFilename().setText("merge_1.gpx");
    }

    public JTextField getFilename() {
        return filename;
    }

    public JTextField getFoldername() {
        return foldername;
    }

    public JTextField getPath() {
        return path;
    }

    public DialogDescriptor getDescriptor() {
        if (descriptor == null) {
            descriptor = new DialogDescriptor(MergeForm.this, Bundle.CLT_Merge_Dialog_Title(), true, null);
        }
        return descriptor;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filename = new javax.swing.JTextField();
        foldername = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        path = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();

        filename.setText(org.openide.util.NbBundle.getMessage(MergeForm.class, "MergeForm.filename.text")); // NOI18N

        foldername.setText(org.openide.util.NbBundle.getMessage(MergeForm.class, "MergeForm.foldername.text")); // NOI18N

        jLabel1.setLabelFor(filename);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(MergeForm.class, "MergeForm.jLabel1.text")); // NOI18N
        jLabel1.setToolTipText(org.openide.util.NbBundle.getMessage(MergeForm.class, "MergeForm.jLabel1.toolTipText")); // NOI18N

        jLabel2.setLabelFor(foldername);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(MergeForm.class, "MergeForm.jLabel2.text")); // NOI18N

        path.setEditable(false);
        path.setText(org.openide.util.NbBundle.getMessage(MergeForm.class, "MergeForm.path.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(MergeForm.class, "MergeForm.jLabel3.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(path, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                    .addComponent(foldername)
                    .addComponent(filename))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filename, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(foldername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(path, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField filename;
    private javax.swing.JTextField foldername;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField path;
    // End of variables declaration//GEN-END:variables

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateForm(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateForm(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        updateForm(e);
    }

    public boolean checkIfValid() {
        getDescriptor().setValid(true);
        if (getDescriptor().getNotificationLineSupport() != null) {
            descriptor.getNotificationLineSupport().clearMessages();
        }

        String filenameString = getFilename().getText();
        boolean result = true;
        if (filenameString == null
                || filenameString.length() == 0
                || filenameString.contains("/")
                || filenameString.contains("\\")) {
            getDescriptor().setValid(false);
            getDescriptor().getNotificationLineSupport().setErrorMessage(Bundle.CLT_Merge_Dialog_Error_Message_Filename());
            result = false;
        } else {
            if (parentFolder.getFileObject(path.getText()) != null) {
                getDescriptor().setValid(false);
                getDescriptor().getNotificationLineSupport().setErrorMessage(Bundle.CLT_Merge_Dialog_Error_Message_File_exists());
                result = false;
            }
        }

        return result;
    }

    private void updateForm(DocumentEvent e) {
        updatePath();
        checkIfValid();
    }

    private void updatePath() {
        String pathPattern = (getFoldername().getText() != null && getFoldername().getText().length() > 0) ? "{0}/{1}" : "{1}";
        getPath().setText(MessageFormat.format(pathPattern, getFoldername().getText(), getFilename().getText()));
    }
}
