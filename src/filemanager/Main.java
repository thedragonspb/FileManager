package filemanager;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class Main extends Application {


    private DirectoryView     directoryView;      // представление папок / файлов
    private DirectoryViewTop  directoryViewTop;   // представление пути к текущей дирректории
    private DirectoryTreeView directoryTreeView;  // представление дерева иерархии папок
    private FileInfView       fileInfView;        // представление информации о выделенной папке / файле

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

        // изменение представлений в зависимости от размеров окна
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
                        case States.EV_NEW_CURRENT_DIR :
                            directoryView.setFiles(states.getCurrentDirectory().listFiles());
                            directoryViewTop.update();
                            fileInfView.update();
                            double sceneWidth = root.getWidth() - directoryTreeView.treeView.getWidth();
                            root.setCenter(directoryView.createView((int) sceneWidth));
                            break;
                        case States.EV_NEW_SELECTED_FILE :
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