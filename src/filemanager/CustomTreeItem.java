package filemanager;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

/**
 * Created by thedragonspb on 17.07.17.
 */
public class CustomTreeItem extends TreeItem<String> {
    private boolean isLeaf; // это папка или файл
    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf     = true;

    private File file;
    
    public CustomTreeItem(String value, Image graphic, File file) {
        super(value, new ImageView(graphic));
        this.file = file;
        // смена иконки при открытии / закрытии не пустой папки
        this.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (file.listFiles() != null && file.listFiles().length > 0 && file.getParentFile() != null) {
                    if (newValue) {
                        setGraphic(new ImageView(Icons.openedFolder32));
                    } else {
                        setGraphic(new ImageView(Icons.folderIcon32));
                    }
                }
            }
        });
    }

    @Override
    public ObservableList<TreeItem<String>> getChildren() {
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
            isLeaf = file.isFile();
        }
        return isLeaf;
    }

    private ObservableList<CustomTreeItem> buildChildren(CustomTreeItem treeItem) {
        File f = treeItem.getFile();
        boolean showHiddenFiles = States.getInstance().isShowHiddenFiles();
        if (f != null && f.isDirectory()) {
            File[] files = f.listFiles();
            if (files != null) {
                ObservableList<CustomTreeItem> children = FXCollections.observableArrayList();
                for (File childFile : files) {
                    // показывать скрытые папки или нет
                    if ((childFile.isHidden() & showHiddenFiles) == true || !childFile.isHidden()) {
                        children.add(new CustomTreeItem(childFile.getName(), Icons.getIcon32(childFile), childFile));
                    }
                }
                return children;
            }
        }
        return FXCollections.emptyObservableList();
    }

    public File getFile() {
        return file;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public void setFirstTimeChildren(boolean firstTimeChildren) {
        isFirstTimeChildren = firstTimeChildren;
    }

    public void setFirstTimeLeaf(boolean firstTimeLeaf) {
        isFirstTimeLeaf = firstTimeLeaf;
    }
}