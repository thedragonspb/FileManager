package sample;


import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Main extends Application {

    File choice;
    DirectoryView directoryView;
    DirectoryViewTop directoryViewTop;

    States states;

    @Override
    public void start(Stage primaryStage) throws Exception {

        states = States.getInstance();

        directoryView = new DirectoryView();
        directoryViewTop = new DirectoryViewTop();

        BorderPane root  = new BorderPane();

        Button c = new Button("Load Folder");
        c.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                DirectoryChooser dc = new DirectoryChooser();
                dc.setInitialDirectory(new File(System.getProperty("user.home")));
                choice = dc.showDialog(primaryStage);
                if(choice == null || ! choice.isDirectory()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Could not open directory");
                    alert.setContentText("The file is invalid.");
                    alert.showAndWait();
                } else {
                    ArrayList<File> history = States.getInstance().getHistory();
                    if (!history.contains(choice)) {
                        history.clear();
                        File temp = choice;
                        while (temp.getParentFile() != null) {
                            history.add(temp);
                            temp = temp.getParentFile();
                        }
                    }
                    states.setCurrentFile(choice);
                    root.setBottom(directoryViewTop.getView());
                }
            }
        });

        root.setTop(c);

        root.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (choice != null) {
                    Node node = directoryView.createGridPane(newValue.intValue());
                    if (node != null)
                        root.setCenter(node);
                }
            }
        });


        States.getInstance().addObserver(new Observer() {
            public void update(Observable o, Object arg) {
                if (o instanceof States) {
                    switch ((int) arg) {
                        case States.EV_NEW_CUR_FILE :
                            directoryView.setFiles(states.getCurrentFile().listFiles());
                            directoryViewTop.update();
                            root.setCenter(directoryView.createGridPane((int) root.getWidth()));
                            break;
                    }
                }
            }
        });

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 480));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}