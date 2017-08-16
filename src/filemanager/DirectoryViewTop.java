package filemanager;

import filemanager.event.BaseController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by thedragonspb on 10.07.17.
 */
public class DirectoryViewTop extends VBox {

    private Button next = new Button(">");
    private Button prev = new Button("<");
    private Button newDirectory = new Button("+ Новая папка");
    private CheckBox showHiddenFiles = new CheckBox("Скрытые объекты");

    private HBox pathView;
    private HBox pathControllersView;
    private HBox controllersView;

    private States states;

    BaseController controller;

    private File currentDirectory = null;

    public DirectoryViewTop(BaseController controller) {
        this.controller = controller;
        states = States.getInstance();
        
        prev.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (currentDirectory != null && currentDirectory.getParentFile() != null) {
                    controller.onNewCurDirectory(currentDirectory.getParentFile());
                }
            }
        });

        next.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ArrayList<File> history = states.getHistory();
                if (history.indexOf(currentDirectory) > 0) {
                    controller.onNewCurDirectory(history.get(history.indexOf(currentDirectory) - 1));
                }
            }
        });

        newDirectory.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createNewFolderDialog();
            }
        });

        showHiddenFiles.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                States states = States.getInstance();
                states.setShowHiddenFiles(showHiddenFiles.isSelected());
                if (currentDirectory != null) {
                    controller.onNewCurDirectory(currentDirectory);
                }
            }
        });

        prev.setStyle(btnStyle1);
        next.setStyle(btnStyle1);
        newDirectory.setStyle(btnStyle1);
        showHiddenFiles.setStyle("-fx-text-fill: white;");

        pathView = new HBox();
        pathControllersView = new HBox(prev, next, pathView);

        controllersView = new HBox();
        controllersView.getChildren().addAll(showHiddenFiles, newDirectory);
        controllersView.setAlignment(Pos.CENTER_RIGHT);
        controllersView.setSpacing(10);

        getChildren().addAll(pathControllersView, controllersView);
        setSpacing(5);
        setPadding(new Insets(0,5,5,0));
    }

    public void update(File curDir) {
        currentDirectory = curDir;
        updateHistory(curDir);
        createPath();
    }

    public void createNewFolderDialog() {
        TextInputDialog dialog = new TextInputDialog("Имя папки");
        dialog.setTitle("Новая папка");
        dialog.setHeaderText(null);
        dialog.setContentText("Введите имя папки:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && currentDirectory != null) {
            File newFile = new File(currentDirectory.getPath() + "/" + result.get());
            if (newFile.mkdir()) {
                controller.onCreateDirectory(newFile);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Ошибка создания папки");
                alert.setContentText(null);
                alert.show();
            }
        }
    }

    public void updateHistory(File file) {
        ArrayList<File> history = States.getInstance().getHistory();
        if (!history.contains(file)) {
            history.clear();
            File temp = file;
            do {
                history.add(temp);
                temp = temp.getParentFile();
            } while (temp != null);
        }
    }

    private void createPath() {
        ArrayList<File> files = states.getHistory();
        pathView.getChildren().clear();
        for (int i = files.size() - 1; i >= 0; i--) {
            PathViewItem btn = new PathViewItem(files.get(i));
            if (files.get(i).equals(currentDirectory)) {
                btn.setDisable(true);
            }
            pathView.getChildren().add(btn);
        }
    }

    class PathViewItem extends javafx.scene.control.Button {
        File file;
        public PathViewItem(File file) {
            String name = file.getName();
            if (file.getParentFile() == null) {
                FileSystemView fsv = FileSystemView.getFileSystemView();
                name = fsv.getSystemDisplayName(file);
            }
            setText(name);
            this.file = file;
            this.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    controller.onNewCurDirectory(file);
                }
            });
            setStyle(pathStyle);
        }
    }

    public String pathStyle =
            "-fx-background-color: #000000,             \n" +
                    "linear-gradient(#fcfcfc, #f3f3f3), \n" +
                    "linear-gradient(#fcfcfc, #f3f3f3), \n" +
                    "linear-gradient(#fcfcfc, #f3f3f3); \n" +
            "-fx-background-insets: 0,1,2,3;            \n" +
            "-fx-background-radius: 0,0,0,0;            \n" +
            "-fx-padding: 6 15 6 15;                    \n" +
            "-fx-text-fill: black;                      \n" +
            "-fx-font-size: 12px;";

    public String btnStyle1 =
            "-fx-background-color: #000000,             \n" +
                    "linear-gradient(#7ebcea, #2f4b8f), \n" +
                    "linear-gradient(#426ab7, #263e75), \n" +
                    "linear-gradient(#395cab, #223768); \n" +
            "-fx-background-insets: 0,1,2,3;            \n" +
            "-fx-background-radius: 0,0,0,0;            \n" +
            "-fx-padding: 6 15 6 15;                    \n" +
            "-fx-text-fill: white;                      \n" +
            "-fx-font-size: 12px;";
}