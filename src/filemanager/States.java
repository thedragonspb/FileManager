package filemanager;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by thedragonspb on 11.07.17.
 */
public class States extends Observable {

    public static final int EV_NEW_CURRENT_DIR   = 1;
    public static final int EV_NEW_SELECTED_FILE = 2;

    private static States ourInstance;

    private boolean isWindows = false;
    private File selectedFile     = null;
    private File currentDirectory = null;
    private ArrayList<File> history;

    private boolean showHiddenFiles = false;

    private States() {
        history = new ArrayList<>();
        String OS = System.getProperty("os.name").toLowerCase();
        isWindows = (OS.indexOf("win") >= 0);
    }

    public File getCurrentDirectory() {
        return currentDirectory;
    }

    public void setCurrentDirectory(File currentDirectory) {
        this.currentDirectory = currentDirectory;
        this.selectedFile     = currentDirectory;
    }

    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    public ArrayList<File> getHistory() {
        return history;
    }

    public boolean isShowHiddenFiles() {
        return showHiddenFiles;
    }

    public void setShowHiddenFiles(boolean showHiddenFiles) {
        this.showHiddenFiles = showHiddenFiles;
    }

    public boolean isWindows() {
        return isWindows;
    }

    public static States getInstance() {
        if (null == ourInstance) {
            ourInstance = new States();
        }
        return ourInstance;
    }
}