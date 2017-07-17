package filemanager;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;

import javax.swing.filechooser.FileSystemView;
import java.io.File;

/**
 * Created by thedragonspb on 14.07.17.
 */
public class DirectoryTreeView {

    TreeView<String> treeView = null;

    public DirectoryTreeView() {
        treeView = buildFileSystemBrowser();
        treeView.getSelectionModel().selectedItemProperty().addListener( new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    CustomTreeItem item = (CustomTreeItem) newValue;
                    File file = item.getFile();
                    if (file == null)
                        return;
                    if (file.isDirectory()) {
                        DirectoryItem.updateHistory(file);
                    } else {
                        DirectoryItem.openFile(file);
                        States.getInstance().setSelectedFile(file);
                    }
                }
            }
        });
    }

    private TreeView buildFileSystemBrowser() {
        File[] roots = File.listRoots();
        FileSystemView fsv = FileSystemView.getFileSystemView();

        Image imgPC = Icons.computer;
        Image imgDrive = Icons.hardDrive32;

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
        return new TreeView<>(root);
    }

    public TreeView<String> getTreeView() {
        return treeView;
    }
}