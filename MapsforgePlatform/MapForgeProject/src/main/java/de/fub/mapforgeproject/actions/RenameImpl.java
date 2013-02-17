/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.mapforgeproject.actions;

import de.fub.mapforgeproject.MapsForgeProject;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.MoveOrRenameOperationImplementation;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Serdar
 */
public class RenameImpl implements MoveOrRenameOperationImplementation {

    private final MapsForgeProject project;

    public RenameImpl(MapsForgeProject project) {
        this.project = project;
    }

    @Override
    public void notifyRenaming() throws IOException {
    }

    @Override
    public void notifyRenamed(String nueName) throws IOException {
    }

    @Override
    public void notifyMoving() throws IOException {
    }

    @Override
    public void notifyMoved(Project original, File originalPath, String nueName) throws IOException {
    }

    @Override
    public List<FileObject> getMetadataFiles() {
        return Arrays.asList(project.getProjectDirectory());
    }

    @Override
    public List<FileObject> getDataFiles() {
        return Arrays.asList(project.getProjectDirectory());
    }
}