package filemanager.event;

import filemanager.*;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by thedragonspb on 06.08.17.
 */
public class BaseController {

    private DirectoryView directoryView         = null;
    private DirectoryViewTop directoryViewTop   = null;
    private DirectoryTreeView directoryTreeView = null;
    private FileInfView fileInfView             = null;

    private File curDirectory;

    public void onNewCurDirectory(File curDir) {
        curDirectory = curDir;
        directoryViewUpdate(curDir);
        directoryTopViewUpdate(curDir);
        fileInfViewUpdate(curDir);
    }

    public void onNewCurFile(File newFile) {
        if (fileInfView != null) {
            fileInfView.update(newFile);
        }
    }

    public void onRenameDirectory(File file) {
        onNewCurDirectory(file.getParentFile());
    }

    public void onDeleteDirectory(File file) {
        onNewCurDirectory(file.getParentFile());
    }

    public void onCreateDirectory(File file) {
        onNewCurDirectory(file.getParentFile());
        onNewCurFile(file);
    }

    public void onShowHiddenFiles(boolean isShowHiddenFiles) {

    }

    private void directoryViewUpdate(File file) {
        if (directoryView != null) {
            directoryView.update(file);
        }
    }

    private void directoryTopViewUpdate(File file) {
        if (directoryViewTop != null) {
            directoryViewTop.update(file);
        }
    }

    private void fileInfViewUpdate(File file) {
        if (fileInfView != null) {
            fileInfView.update(file);
        }
    }

    public void setDirectoryView(DirectoryView directoryView) {
        this.directoryView = directoryView;
    }

    public void setDirectoryViewTop(DirectoryViewTop directoryViewTop) {
        this.directoryViewTop = directoryViewTop;
    }

    public void setDirectoryTreeView(DirectoryTreeView directoryTreeView) {
        this.directoryTreeView = directoryTreeView;
    }

    public void setFileInfView(FileInfView fileInfView) {
        this.fileInfView = fileInfView;
    }
}
