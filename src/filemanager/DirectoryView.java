package filemanager;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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

    private int oldSceneWidth;

    private VBox selectedItem = null;

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
        Image image = null;
        items.clear();
        ArrayList<File> notDirectory = new ArrayList<>();
        boolean showHiddenFiles = States.getInstance().isShowHiddenFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if ((file.isHidden() & showHiddenFiles) == true || !file.isHidden()) {
                    if (file.isDirectory()) {
                        image = Icons.getIcon64(file);
                        items.add(new DirectoryItem(file, image));
                    } else {
                        notDirectory.add(file);
                    }
                }
            }

            for (File file : notDirectory) {
                image = Icons.getIcon64(file);
                items.add(new DirectoryItem(file, image));
            }
        }
    }

    public Node createView(int sceneWidth) {
        //if (Math.abs(sceneWidth - oldSceneWidth) < ITEM_WIDTH + ITEM_PADDING * 2 && sceneWidth > oldSceneWidth)
          //  return null;

        oldSceneWidth = sceneWidth;

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

    class DirectoryItem {
        Label label;
        ImageView imageView;
        VBox vBox = new VBox();

        DirectoryItem(File file, Image image) {
            label = newLabel(file.getName());
            imageView = newImageView(image);
            vBox = new VBox(imageView, label);
            vBox.setAlignment(Pos.CENTER);
            vBox.setPadding(new Insets(ITEM_PADDING, ITEM_PADDING, ITEM_PADDING, ITEM_PADDING));
            vBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(event.getButton().equals(MouseButton.PRIMARY)){
                        switch (event.getClickCount()) {
                            case 1 : {
                                if (selectedItem != null)
                                    selectedItem.setStyle("");
                                selectedItem = vBox;
                                vBox.setStyle("-fx-background-color:#fcf3ba");
                                States.getInstance().setSelectedFile(file);
                                break;
                            }
                            case 2 : {
                                if (file.isDirectory()) {
                                    ArrayList<File> history = States.getInstance().getHistory();
                                    if (!history.contains(file)) {
                                        history.clear();
                                        File temp = file;
                                        do {
                                            history.add(temp);
                                            temp = temp.getParentFile();
                                        } while (temp != null);
                                    }
                                    States.getInstance().setCurrentDirectory(file);
                                }
                                break;
                            }
                        }
                    }
                }
            });
            vBox.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.isSecondaryButtonDown()) {
                        final ContextMenu contextMenu = new ContextMenu();
                        MenuItem rename = new MenuItem("Переименовать");
                        MenuItem delete = new MenuItem("Удалить");
                        MenuItem info = new MenuItem("Свойства");
                        contextMenu.getItems().addAll(delete, rename, info);
                        contextMenu.setWidth(250);

                        if (selectedItem != null)
                            selectedItem.setStyle("");
                        selectedItem = vBox;
                        vBox.setStyle("-fx-background-color:#fcf3ba");
                        States.getInstance().setSelectedFile(file);
                        contextMenu.show(vBox, event.getScreenX(), event.getScreenY());
                    }
                }
            });
        }

        private Label newLabel(String text) {
            Label label = new Label(text);
            label.setWrapText(true);
            label.setPrefWidth(ITEM_WIDTH);
            label.setPrefSize(ITEM_WIDTH,60);
            label.setAlignment(Pos.CENTER);
            return label;
        }

        private ImageView newImageView(Image image) {
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(ITEM_IMG_WIDTH);
            imageView.setFitHeight(ITEM_IMG_WIDTH);
            return imageView;
        }
    }
}