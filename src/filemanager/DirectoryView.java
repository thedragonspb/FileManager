package filemanager;

import filemanager.event.BaseController;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by thedragonspb on 10.07.17.
 */
public class DirectoryView extends ScrollPane {

    public static int ITEM_WIDTH     = 120;
    public static int ITEM_PADDING   = 5;
    
    private GridPane gridPane;

    private ArrayList<DirectoryItem> items = new ArrayList<>();

    public static VBox selectedItem = null;

    BaseController controller;

    private int sceneWidth;
    private int itemsCountInRow = 0;

    public DirectoryView(BaseController controller) {
        this.controller = controller;

        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER_LEFT);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        setContent(gridPane);
        setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    }

    public void update(File dir) {
        File[] files = dir.listFiles();
        items.clear();
        ArrayList<File> notDirectory = new ArrayList<>();
        boolean showHiddenFiles = States.getInstance().isShowHiddenFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if ((file.isHidden() & showHiddenFiles) == true || !file.isHidden()) {
                    if (file.isDirectory()) {
                        items.add(new DirectoryItem(file, Icons.getIcon(file, Icons.MEDIUM_ICON_WIDTH)));
                    } else {
                        notDirectory.add(file);
                    }
                }
            }

            for (File file : notDirectory) {
                items.add(new DirectoryItem(file, Icons.getIcon(file, Icons.MEDIUM_ICON_WIDTH)));
            }

            for (DirectoryItem item : items) {
                item.setController(controller);
            }
        }
        updateView(false);
    }

    public void setSceneWidth(int sceneWidth) {
        this.sceneWidth = sceneWidth;
    }

    public void updateView(boolean update) {

        int buttonWidth = ITEM_WIDTH + ITEM_PADDING * 2;
        int newItemsCountInRow = sceneWidth / buttonWidth;

        if (newItemsCountInRow == itemsCountInRow && update)
            return;

        itemsCountInRow = newItemsCountInRow;

        gridPane.getChildren().clear();

        int i = 1, j = 1;
        for (DirectoryItem item : items) {
            gridPane.add(item, i, j);
            i ++;
            if (i == itemsCountInRow) {
                j++;
                i = 1;
            }
        }
    }
}