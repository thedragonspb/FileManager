package filemanager;

import filemanager.event.BaseController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by thedragonspb on 06.08.17.
 */
public class MainScene {

    Scene scene = null;
    private DirectoryView     directoryView;      // представление папок / файлов
    private DirectoryViewTop  directoryViewTop;   // представление пути к текущей дирректории
    private DirectoryTreeView directoryTreeView;  // представление дерева иерархии папок
    private FileInfView       fileInfView;        // представление информации о выделенной папке / файле

    private BaseController controller = new BaseController();

    public MainScene(int width, int height) {
        scene = createScene(width, height);
    }

    public Scene getScene() {
        return scene;
    }

    private Scene createScene(int width, int height) {
        States states = States.getInstance();

        directoryView     = new DirectoryView(controller);
        directoryViewTop  = new DirectoryViewTop(controller);
        directoryTreeView = new DirectoryTreeView(controller);
        fileInfView       = new FileInfView(controller);

        controller.setDirectoryView(directoryView);
        controller.setDirectoryViewTop(directoryViewTop);
        controller.setDirectoryTreeView(directoryTreeView);
        controller.setFileInfView(fileInfView);

        BorderPane root = new BorderPane();
        root.setTop(directoryViewTop);
        root.setLeft(directoryTreeView);
        root.setBottom(fileInfView);
        root.setCenter(directoryView);

        // изменение представлений в зависимости от размеров окна
        directoryTreeView.prefHeightProperty().bind(root.heightProperty());
        root.getTop().setStyle("-fx-background-color:#16284c");
        root.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int sceneWidth = newValue.intValue();
                int dirTreeWidth = (int) directoryTreeView.getWidth();
                sceneWidth -= dirTreeWidth == 0 ? 250 :  dirTreeWidth;
                directoryView.setSceneWidth(sceneWidth);
                directoryView.updateView(true);
            }
        });

        return  new Scene(root, width, height);
    }

}
