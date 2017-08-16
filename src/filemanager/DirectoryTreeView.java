package filemanager;

import filemanager.event.BaseController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;

import javax.swing.filechooser.FileSystemView;
import java.io.File;

/**
 * Created by thedragonspb on 14.07.17.
 */
public class DirectoryTreeView extends TreeView {

    BaseController controller;

    public DirectoryTreeView(BaseController controller) {
        super(buildFileSystemBrowser());
        this.controller = controller;
        getSelectionModel().selectedItemProperty().addListener( new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    CustomTreeItem item = (CustomTreeItem) newValue;
                    File file = item.getFile();
                    if (file == null)
                        return;
                    if (file.isDirectory()) {
                        controller.onNewCurDirectory(file);
                    } else {
                        DirectoryItem.openFile(file);
                        controller.onNewCurFile(file);
                    }
                }
            }
        });
    }

    private static TreeItem buildFileSystemBrowser() {
        File[] roots = File.listRoots();
        FileSystemView fsv = FileSystemView.getFileSystemView();

        ImageView imgPC    = Icons.getIcon(Icons.computer, Icons.SMALL_ICON_WIDTH);
        ImageView imgDrive = Icons.getIcon(Icons.hardDrive, Icons.SMALL_ICON_WIDTH);

        CustomTreeItem root;
        if (States.getInstance().isWindows()) {
            root = new CustomTreeItem("Компьютер", imgPC, null);
            root.setFirstTimeChildren(false);
            root.setFirstTimeLeaf(false);
            ObservableList<CustomTreeItem> children = FXCollections.observableArrayList();
            for (File drive : roots) {
                if (fsv.getSystemTypeDescription(drive).equals("Локальный диск") ||
                    fsv.getSystemTypeDescription(drive).equals("Local Disk")) {
                    children.add(new CustomTreeItem(fsv.getSystemDisplayName(drive), imgDrive, drive));
                }
            }
            root.getChildren().setAll(children);
        } else {
            root = new CustomTreeItem("Компьютер", imgPC, new File("/"));
        }
        return root;
    }
}