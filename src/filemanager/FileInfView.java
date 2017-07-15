package filemanager;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sun.util.resources.cldr.sbp.LocaleNames_sbp;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by thedragonspb on 14.07.17.
 */
public class FileInfView extends HBox {

    public FileInfView() {
        setPadding(new Insets(10,10,10,10));
        setSpacing(10);
    }

    public void update() {
        getChildren().clear();

        File file = States.getInstance().getSelectedFile();

        Node view = null;
        ImageView img = new ImageView(Icons.getIcon64(file));
        if (file.isDirectory()) {
            view = createFolderInfView(file);
        } else {
            view = createFileInfView(file);
        }

        getChildren().addAll(img, view);
    }

    private Node createFolderInfView(File file) {
        Label name = createLabel(file.getName());
        Label itemsCount = createLabel("" + (file.list() != null ? file.list().length : 0));

        DateFormat da = new SimpleDateFormat("HH:mm:ss  dd.MM.yyyy");
        Label lastModified = createLabel(da.format(new Date(file.lastModified())));

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(10);
        gridPane.setVgap(5);

        gridPane.add(new Label("Папка")               ,0,0);
        gridPane.add(new Label("Кол-во вложений")     ,0,1);
        gridPane.add(new Label("Последнее изменение "),0,2);
        gridPane.add(name                                  ,1,0);
        gridPane.add(itemsCount                            ,1,1);
        gridPane.add(lastModified                          ,1,2);

        return gridPane;
    }

    private Node createFileInfView(File file) {
        Label name = createLabel(file.getName());
        Label length = createLabel(getFileSize(file.length(), true));
        DateFormat da = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        Label lastModified = createLabel(da.format(new Date(file.lastModified())));

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(10);
        gridPane.setVgap(5);

        gridPane.add(new Label("Файл")                ,0,0);
        gridPane.add(new Label("Размер")              ,0,1);
        gridPane.add(new Label("Последнее изменение") ,0,2);
        gridPane.add(name                                  ,1,0);
        gridPane.add(length                                ,1,1);
        gridPane.add(lastModified                          ,1,2);


        return gridPane;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-weight:bold;");
        return label;
    }

    public static String getFileSize(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}