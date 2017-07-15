package filemanager;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by thedragonspb on 10.07.17.
 */
public class DirectoryViewTop {

    private Button next = new Button(">");
    private Button prev = new Button("<");

    private HBox pathView;
    private HBox pathControllersView;
    private HBox controllersView;
    private VBox topView;

    private States states;

    public DirectoryViewTop() {
        states = States.getInstance();
        
        prev.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File currentDirectory   = states.getCurrentDirectory();
                ArrayList<File> history = states.getHistory();
                if (currentDirectory.getParentFile() != null) {
                    currentDirectory = currentDirectory.getParentFile();
                    states.setCurrentDirectory(currentDirectory);
                }
            }
        });

        next.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ArrayList<File> history = states.getHistory();
                File currentDirectory   = states.getCurrentDirectory();
                if (history.indexOf(currentDirectory) > 0) {
                    currentDirectory = history.get(history.indexOf(currentDirectory) - 1);
                    states.setCurrentDirectory(currentDirectory);
                }
            }
        });

        prev.setStyle(btnStyle);
        next.setStyle(btnStyle);

        pathView = new HBox();
        pathControllersView = new HBox(prev, next, pathView);
        controllersView = new HBox();
        controllersView.getChildren().addAll(new Button(), new Button());
        topView = new VBox(pathControllersView);
    }

    public Node getView() {
        return topView;
    }

    public void update() {
        createPath();
    }

    private void createPath() {
        ArrayList<File> files = states.getHistory();
        pathView.getChildren().clear();
        for (int i = files.size() - 1; i >= 0; i--) {
            PathView btn = new PathView(files.get(i));
            if (files.get(i).equals(states.getCurrentDirectory())) {
                btn.setDisable(true);
            }
            pathView.getChildren().add(btn);
        }
    }

    class PathView extends javafx.scene.control.Button {
        File file;
        public PathView(File file) {
            String name = file.getName();
            if (name.equals(""))
                name = "/";
            setText(name);
            this.file = file;
            this.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    states.setCurrentDirectory(file);
                }
            });
            setStyle(
                    "-fx-background-color: rgba(0,0,0,0.08),\n" +
                    "linear-gradient(#9a9a9a, #909090),\n" +
                    "linear-gradient(white 0%, #f3f3f3 50%, #ececec 51%, #f2f2f2 100%);\n" +
                    "-fx-background-radius: 0;\n" +
                    "-fx-text-fill: #242d35;\n" +
                    "-fx-font-size: 14px;"
            );
        }
    }

    public String btnStyle =
            "-fx-background-color: \n" +
                    "rgba(0,0,0,0.08),\n" +
                    "linear-gradient(#5a61af, #51536d),\n" +
                    "linear-gradient(#e4fbff 0%,#cee6fb 10%, #a5d3fb 50%, #88c6fb 51%, #d5faff 100%);\n" +
                    "-fx-background-insets: 0 0 -1 0,0,1;\n" +
                    "-fx-background-radius: 0;\n" +
                    "-fx-text-fill: #242d35;\n" +
                    "-fx-font-size: 14px;";
}