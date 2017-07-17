package filemanager;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static filemanager.DirectoryView.*;

/**
 * Created by thedragonspb on 16.07.17.
 */
class DirectoryItem {

    Label name;
    ImageView imageView;
    VBox vBox = new VBox();

    DirectoryItem(File file, Image image) {
        name = newLabel(file.getName());
        imageView = newImageView(image);
        vBox = new VBox(imageView, name);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(ITEM_PADDING, ITEM_PADDING, ITEM_PADDING, ITEM_PADDING));
        vBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton().equals(MouseButton.PRIMARY)){

                    // обработка события нажатия но папку/файл левой кнопки мыши
                    switch (event.getClickCount()) {
                        // одно нажатие
                        case 1 : {
                            setSelectedItem(file);
                            break;
                        }
                        // дабл клик
                        case 2 : {
                            // если это папка, задаем ее текущей и обновляем путь к ней из корня
                            if (file.isDirectory()) {
                                updateHistory(file);
                            } else {
                                openFile(file);
                            }
                            break;
                        }
                    }
                }
            }
        });
        // событие нажатие правой кнопки мыши
        vBox.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isSecondaryButtonDown()) {
                    setSelectedItem(file);
                    createContextMenu(file).show(vBox, event.getScreenX(), event.getScreenY());
                }
            }
        });
    }

    public static void updateHistory(File file) {
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

    public static void openFile(File file) {
        if( Desktop.isDesktopSupported() ) {
            new Thread(() -> {
                try {
                    Desktop.getDesktop().browse(file.toURI());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }).start();
        }
    }

    private void setSelectedItem(File file) {
        // устанавливаем цвет предыдущего выделенного элемента обычным и
        // выделяем новый
        if (selectedItem != null)
            selectedItem.setStyle("");
        selectedItem = vBox;
        vBox.setStyle("-fx-background-color:#fcf3ba");
        States.getInstance().setSelectedFile(file);
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

    private ContextMenu createContextMenu(File file) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem rename = new MenuItem("Переименовать");
        MenuItem delete = new MenuItem("Удалить");
        contextMenu.getItems().addAll(rename, delete);

        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File parent = file.getParentFile();
                if (file.delete()) {
                    States.getInstance().getHistory().clear();
                    updateHistory(parent);
                    States.getInstance().setCurrentDirectory(States.getInstance().getCurrentDirectory());
                } else {
                    String temp = file.isDirectory() ? "эту папку" : "этот файл";
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText("Ошибка при удалении");
                    alert.setContentText("Невозможно удалить " + temp);
                    alert.show();
                }
            }
        });

        rename.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TextInputDialog dialog = new TextInputDialog(file.getName());
                dialog.setTitle("Переименование");
                dialog.setHeaderText(null);
                dialog.setContentText("Введите имя :");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    String path = States.getInstance().getCurrentDirectory().getPath();
                    File newName = new File(path + "/" + result.get());
                    if (file.renameTo(newName)) {
                        States.getInstance().getHistory().clear();
                        updateHistory(file.getParentFile());
                        States.getInstance().setCurrentDirectory(States.getInstance().getCurrentDirectory());
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Ошибка при переименовании");
                        alert.setContentText("Невозможно изменить название");
                        alert.show();
                    }
                }
            }
        });
        return contextMenu;
    }
}