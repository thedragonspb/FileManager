package filemanager;

import filemanager.event.BaseController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static filemanager.DirectoryView.*;

/**
 * Created by thedragonspb on 16.07.17.
 */
class DirectoryItem extends VBox {

    Label name;
    BaseController controller;

    DirectoryItem(File file, ImageView imageView) {
        name = newLabel(file.getName());
        getChildren().addAll(imageView, name);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(ITEM_PADDING, ITEM_PADDING, ITEM_PADDING, ITEM_PADDING));
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {

                    // обработка события нажатия но папку/файл левой кнопки мыши
                    switch (event.getClickCount()) {
                        // одно нажатие
                        case 1: {
                            setSelectedItem(file);
                            break;
                        }
                        // дабл клик
                        case 2: {
                            // если это папка, задаем ее текущей и обновляем путь к ней из корня
                            if (file.isDirectory()) {
                                controller.onNewCurDirectory(file);
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
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isSecondaryButtonDown()) {
                    setSelectedItem(file);
                    createContextMenu(file).show(getParent(), event.getScreenX(), event.getScreenY());
                }
            }
        });
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
        selectedItem = this;
        setStyle("-fx-background-color:#fcf3ba");
        controller.onNewCurFile(file);
    }

    private Label newLabel(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setPrefWidth(ITEM_WIDTH);
        label.setPrefSize(ITEM_WIDTH,60);
        label.setAlignment(Pos.CENTER);
        return label;
    }

    private ContextMenu createContextMenu(File file) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem rename = new MenuItem("Переименовать");
        MenuItem delete = new MenuItem("Удалить");
        contextMenu.getItems().addAll(rename, delete);

        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (file.delete()) {
                    controller.onDeleteDirectory(file);
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
                    String path = file.getParentFile().getPath();
                    File newName = new File(path + "/" + result.get());
                    if (file.renameTo(newName)) {
                        controller.onRenameDirectory(file);
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

    public void setController(BaseController controller) {
        this.controller = controller;
    }
}