package filemanager;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by thedragonspb on 10.07.17.
 */
public class DirectoryView {

    public static int ITEM_WIDTH     = 120;
    public static int ITEM_IMG_WIDTH = 60;
    public static int ITEM_PADDING   = 5;
    
    private ScrollPane scrollPane;
    private GridPane gridPane;

    private ArrayList<DirectoryItem> items = new ArrayList<>();

    public static VBox selectedItem = null;

    public DirectoryView() {
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER_LEFT);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        scrollPane = new ScrollPane();
        scrollPane.setContent(gridPane);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    }

    public void setFiles(File[] files) {
        items.clear();
        ArrayList<File> notDirectory = new ArrayList<>();
        boolean showHiddenFiles = States.getInstance().isShowHiddenFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if ((file.isHidden() & showHiddenFiles) == true || !file.isHidden()) {
                    if (file.isDirectory()) {
                        Image image = Icons.getIcon64(file);
                        items.add(new DirectoryItem(file, image));
                    } else {
                        notDirectory.add(file);
                    }
                }
            }

            for (File file : notDirectory) {
                Image image = Icons.getIcon64(file);
                items.add(new DirectoryItem(file, image));
            }
        }
    }

    public Node createView(int sceneWidth) {

        int buttonWidth = ITEM_WIDTH + ITEM_PADDING * 2;
        int buttonsCountInRow = sceneWidth / buttonWidth;

        gridPane.getChildren().clear();

        int i = 1, j = 1;
        for (DirectoryItem item : items) {
            gridPane.add(item.vBox, i, j);
            i ++;
            if (i == buttonsCountInRow) {
                j++;
                i = 1;
            }
        }
        return scrollPane;
    }
}