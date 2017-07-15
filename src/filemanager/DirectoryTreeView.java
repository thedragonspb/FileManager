package filemanager;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.ArrayList;

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
                    File selectedItem = ((TreeItem<File>) newValue).getValue();
                    if (selectedItem.isDirectory()) {
                        ArrayList<File> history = States.getInstance().getHistory();
                        if (!history.contains(selectedItem)) {
                            history.clear();
                            File temp = selectedItem;
                            do {
                                history.add(temp);
                                temp = temp.getParentFile();
                            } while (temp != null);
                        }
                        States.getInstance().setCurrentDirectory(selectedItem);
                    } else {
                        States.getInstance().setSelectedFile(selectedItem);
                    }
                }
            }
        });
    }

    private TreeView buildFileSystemBrowser() {
        File file = new File("/");
        Image img = file.listFiles().length > 0 ? Icons.folderIcon32 : Icons.emptyFolderIcon32;
        TreeItem<File> root = createNode(new File("/"), img);
        return new TreeView<>(root);
    }

    private TreeItem<File> createNode(final File f, Image img) {
        return new TreeItem<File>(f, new ImageView(img)) {

            private boolean isLeaf;
            private boolean isFirstTimeChildren = true;
            private boolean isFirstTimeLeaf = true;

            @Override
            public ObservableList<TreeItem<File>> getChildren() {
                if (isFirstTimeChildren) {
                    isFirstTimeChildren = false;
                    super.getChildren().setAll(buildChildren(this));
                }
                return super.getChildren();
            }

            @Override
            public boolean isLeaf() {
                if (isFirstTimeLeaf) {
                    isFirstTimeLeaf = false;
                    File f = (File) getValue();
                    isLeaf = f.isFile();
                }
                return isLeaf;
            }

            private ObservableList<TreeItem<File>> buildChildren(TreeItem<File> TreeItem) {
                File f = TreeItem.getValue();
                boolean showHiddenFiles = States.getInstance().isShowHiddenFiles();
                if (f != null && f.isDirectory()) {
                    File[] files = f.listFiles();
                    if (files != null) {
                        ObservableList<TreeItem<File>> children = FXCollections.observableArrayList();

                        for (File childFile : files) {
                            if ((childFile.isHidden() & showHiddenFiles) == true ||
                                 !childFile.isHidden())
                                children.add(createNode(childFile, Icons.getIcon32(childFile)));

                        }

                        return children;
                    }
                }
                return FXCollections.emptyObservableList();
            }
        };
    }

    public TreeView<String> getTreeView() {
        return treeView;
    }
}