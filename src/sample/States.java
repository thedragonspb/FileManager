package sample;

import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by thedragonspb on 11.07.17.
 */
public class States extends Observable {

    public static final int EV_NEW_CUR_FILE = 1;

    private static States ourInstance;

    public static States getInstance() {
        if (null == ourInstance) {
            ourInstance = new States();
        }
        return ourInstance;
    }

    private File currentFile = null;
    private ArrayList<File> history;

    private States() {
        history = new ArrayList<>();
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
        setChanged();
        notifyObservers(EV_NEW_CUR_FILE);
    }

    public ArrayList<File> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<File> history) {
        this.history = history;
    }


}
