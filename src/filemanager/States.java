package filemanager;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by thedragonspb on 11.07.17.
 */
public class States extends Observable {

    public static final int EV_NEW_CUR_DIR = 1;
    public static final int EV_NEW_SELL_FILE = 2;

    private static States ourInstance;

    private File selectedFile     = null;
    private File currentDirectory = null;
    private ArrayList<File> history;

    private boolean showHiddenFiles = false;

    private States() {
        history = new ArrayList<>();
    }

    public File getCurrentDirectory() {
        return currentDirectory;
    }

    public void setCurrentDirectory(File currentDirectory) {
        this.currentDirectory = currentDirectory;
        this.selectedFile = currentDirectory;
        setChanged();
        notifyObservers(EV_NEW_CUR_DIR);
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
        setChanged();
        notifyObservers(EV_NEW_SELL_FILE);
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

    public static States getInstance() {
        if (null == ourInstance) {
            ourInstance = new States();
        }
        return ourInstance;
    }
}