package filemanager;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.Observable;
import java.util.Observer;

public class Main extends Application {

    private DirectoryView     directoryView;
    private DirectoryViewTop  directoryViewTop;
    private DirectoryTreeView directoryTreeView;
    private FileInfView       fileInfView;

    @Override
    public void start(Stage primaryStage) {

        States states = States.getInstance();

        directoryView     = new DirectoryView();
        directoryViewTop  = new DirectoryViewTop();
        directoryTreeView = new DirectoryTreeView();
        fileInfView       = new FileInfView();

        BorderPane root = new BorderPane();
        root.setTop(directoryViewTop.getView());
        root.setBottom(fileInfView);
        root.setLeft(directoryTreeView.getTreeView());

        directoryTreeView.getTreeView().prefHeightProperty().bind(root.heightProperty());
        root.getTop().setStyle("-fx-background-color:#16284c");
        root.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (states.getCurrentDirectory() != null) {
                    int sceneWidth = newValue.intValue() - (int) directoryTreeView.treeView.getWidth();
                    Node node = directoryView.createView(sceneWidth);
                    if (node != null)
                        root.setCenter(node);
                }
            }
        });

        States.getInstance().addObserver(new Observer() {
            public void update(Observable o, Object arg) {
                if (o instanceof States) {
                    switch ((int) arg) {
                        case States.EV_NEW_CUR_DIR :
                            directoryView.setFiles(states.getCurrentDirectory().listFiles());
                            directoryViewTop.update();
                            fileInfView.update();
                            double sceneWidth = root.getWidth() - directoryTreeView.treeView.getWidth();
                            root.setCenter(directoryView.createView((int) sceneWidth));
                            break;
                        case States.EV_NEW_SELL_FILE :
                            fileInfView.update();
                            break;
                    }
                }
            }
        });

        primaryStage.setTitle("File Manager");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setMinWidth(550);
        primaryStage.setMinHeight(450);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}