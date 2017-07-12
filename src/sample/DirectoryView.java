package sample;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by thedragonspb on 10.07.17.
 */
public class DirectoryView {

    ScrollPane scrollPane;
    GridPane gridPane;

    ArrayList<DirectoryItem> items = new ArrayList<>();

    Image folderIcon = new Image("folder.png");
    Image fileIcon   = new Image("file.png");
    Image imageIcon  = new Image("photo.png");

    private int oldSceneWidth;

    private VBox selectedItem = null;


    public DirectoryView() {
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER_LEFT);
        gridPane.setHgap(25);
        gridPane.setVgap(25);

        scrollPane = new ScrollPane();
        scrollPane.setContent(gridPane);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    }

    public void setFiles(File[] files) {
        Image image = null;
        items.clear();
        ArrayList<File> notDirectory = new ArrayList<>();

        for (File file : files) {
            if (file.isDirectory()) {
                image = folderIcon;
                items.add(new DirectoryItem(file, image));
            } else {
                notDirectory.add(file);
            }
        }

        for (File file : notDirectory) {
            image = isImage(file) ? imageIcon : fileIcon;
            items.add(new DirectoryItem(file, image));
        }
    }

    public Node createGridPane(int sceneWidth) {
        if (Math.abs(sceneWidth - oldSceneWidth) < 120 && sceneWidth > oldSceneWidth)
            return null;

        oldSceneWidth = sceneWidth;

        int buttonWidth = 120;
        int buttonCountInRow = sceneWidth / buttonWidth;

        gridPane.getChildren().clear();

        int i = 1, j = 1;
        for (DirectoryItem item : items) {
            gridPane.add(item.vBox, i, j);
            i ++;
            if (i == buttonCountInRow) {
                j++;
                i = 1;
            }
        }

        return scrollPane;
    }

    public boolean isImage(File file) {
        try {
            if (ImageIO.read(file) == null) {
                return false;
            }
            return true;
        } catch(IOException ex) {
            return false;
        }
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
            vBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(event.getButton().equals(MouseButton.PRIMARY)){
                        switch (event.getClickCount()) {
                            case 1 :
                                if (selectedItem != null)
                                    selectedItem.setStyle("");
                                selectedItem = vBox;
                                vBox.setStyle("-fx-background-color:#d6fff4");
                                break;

                            case 2 :
                                if (file.isDirectory()) {
                                    ArrayList<File> history = States.getInstance().getHistory();
                                    if (!history.contains(file)) {
                                        history.clear();
                                        File temp = file;
                                        while (temp.getParentFile() != null) {
                                            history.add(temp);
                                            temp = temp.getParentFile();
                                        }
                                    }
                                    States.getInstance().setCurrentFile(file);
                                }
                                break;
                        }
                    }
                }
            });
        }

        private Label newLabel(String text) {
            Label label = new Label(text);
            label.setWrapText(true);
            label.setPrefWidth(120);
            label.setPrefSize(120,40);
            label.setAlignment(Pos.CENTER);
            return label;
        }

        private ImageView newImageView(Image image) {
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(80);
            imageView.setFitHeight(80);
            return imageView;
        }
    }
}