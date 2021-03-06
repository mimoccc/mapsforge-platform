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
package de.fub.maps.project.snapshot.api;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

/**
 * A abstract class, which implements the ComponentSnapShotExporter interface an
 * provides some utility methods for sub classes, like the creates of a
 * fileChooser and implementing the Comparable interface.
 *
 * @author Serdar
 */
public abstract class AbstractComponentSnapShotExporter implements ComponentSnapShotExporter {

    @Override
    public int compareTo(ComponentSnapShotExporter o) {
        return getName().compareToIgnoreCase(o.getName());
    }

    protected File showFileChoose(String fileExtension) {
        File selectedFile = null;
        JFileChooser fileChooser = new FileChooserBuilder(ComponentSnapShotExporter.class)
                .addFileFilter(new FileFilterImpl(fileExtension))
                .setSelectionApprover(new SelectionApproverImpl()).createFileChooser();
        int response = fileChooser.showSaveDialog(null);
        if (JFileChooser.APPROVE_OPTION == response) {
            selectedFile = fileChooser.getSelectedFile();
            if (selectedFile != null) {
                if (!selectedFile.getName().endsWith(MessageFormat.format(".{0}", fileExtension))) {
                    selectedFile = new File(MessageFormat.format("{0}.{1}", selectedFile.getAbsolutePath(), fileExtension));
                }
                try {
                    if (!selectedFile.exists()) {
                        selectedFile.createNewFile();
                    }
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
        return selectedFile;
    }

    private static class FileFilterImpl extends FileFilter {

        private final String fileExt;

        public FileFilterImpl(String fileExtension) {
            this.fileExt = fileExtension;
        }

        @Override
        public boolean accept(File file) {
            FileObject fileObject = FileUtil.toFileObject(file);
            if (fileObject != null) {
                return fileObject.isFolder() || fileObject.isData() && fileExt.equalsIgnoreCase(fileObject.getExt());
            } else {
                return file.isDirectory() || fileExt.equalsIgnoreCase(file.getName().substring(file.getName().lastIndexOf(".") + 1));
            }
        }

        @Override
        public String getDescription() {
            return MessageFormat.format("*.{0}", fileExt);
        }
    }

    @NbBundle.Messages({
        "# {0} - filename",
        "CLT_Approve_Message=Do you want to overwite the file {0}"
    })
    private static class SelectionApproverImpl implements FileChooserBuilder.SelectionApprover {

        public SelectionApproverImpl() {
        }

        @Override
        public boolean approve(File[] selection) {
            boolean result = true;
            for (File file : selection) {
                if (file.exists()) {
                    NotifyDescriptor.Confirmation nd = new NotifyDescriptor.Confirmation(Bundle.CLT_Approve_Message(file.getName()));
                    Object notify = DialogDisplayer.getDefault().notify(nd);
                    if (!NotifyDescriptor.Confirmation.OK_OPTION.equals(notify)) {
                        result = false;
                    }
                }
            }
            return result;
        }
    }
}
