package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thedragonspb on 10.07.17.
 */
public class DirectoryViewTop {

    private Button next =  new Button(">");
    private Button prev =  new Button("<");

    private HBox pathView;
    private HBox view;

    private States states;

    public DirectoryViewTop() {
        states = States.getInstance();
        
        prev.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File currentFile = states.getCurrentFile();
                ArrayList<File> history = states.getHistory();
                if (currentFile.getParentFile() != null) {
                    currentFile = currentFile.getParentFile();
                    states.setCurrentFile(currentFile);
                }

            }
        });

        next.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ArrayList<File> history = states.getHistory();
                File currentFile = states.getCurrentFile();
                if (history.indexOf(currentFile) > 0) {
                    currentFile = history.get(history.indexOf(currentFile) - 1);
                    states.setCurrentFile(currentFile);
                }
            }
        });

        pathView = new HBox();
        view = new HBox(prev, next, pathView);
    }

    public Node getView() {
        return view;
    }

    public void update() {
        createPath();
    }

    private void createPath() {
        ArrayList<File> files = states.getHistory();
        pathView.getChildren().clear();
        System.out.println(files);
        for (int i = files.size() - 1; i >= 0; i--) {
            PathView btn = new PathView(files.get(i));
            if (files.get(i).equals(states.getCurrentFile())) {
                btn.setDisable(true);
            }
            pathView.getChildren().add(btn);
        }
    }

    class PathView extends javafx.scene.control.Button {
        File file;
        public PathView(File file) {
            super(file.getName());
            this.file = file;
            this.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    states.setCurrentFile(file);
                }
            });
        }
    }
}