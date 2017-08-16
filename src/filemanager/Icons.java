package filemanager;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.HashMap;

/**
 * Created by thedragonspb on 13.07.17.
 */
public class Icons {

    public static final int SMALL_ICON_WIDTH    = 35;
    public static final int MEDIUM_ICON_WIDTH   = 60;

    public static Image openedFolder     = new Image("icon64/opened-folder.png");
    public static Image folderIcon       = new Image("icon64/folder.png");
    public static Image emptyFolderIcon  = new Image("icon64/empty_folder.png");
    public static Image unknownFile      = new Image("icon64/file.png");
    public static Image hardDrive        = new Image("icon64/hard-drive.png");
    public static Image computer         = new Image("icon64/computer.png");

    private static final HashMap<String, Image> icons64 = new HashMap<>();

    static {
        icons64.put("avi" , null);         icons64.put("css" , null);
        icons64.put("csv" , null);         icons64.put("dbf" , null);
        icons64.put("doc" , null);         icons64.put("exe" , null);
        icons64.put("html", null);         icons64.put("iso" , null);
        icons64.put("js"  , null);         icons64.put("jpg" , null);
        icons64.put("mp3" , null);         icons64.put("mp4" , null);
        icons64.put("pdf" , null);         icons64.put("png" , null);
        icons64.put("psd" , null);         icons64.put("rar" , null);
        icons64.put("txt" , null);         icons64.put("zip" , null);
        icons64.put("xls" , null);         icons64.put("xml" , null);
        icons64.put("zip" , null);         icons64.put("java", null);
    }

    public static ImageView getIcon(File file) {
        Image image = unknownFile;
        if (file.isDirectory()) {
            if (file.listFiles() != null && file.listFiles().length > 0) {
                image = folderIcon;
            } else {
                image = emptyFolderIcon;
            }
        } else {
            String ext = getFileExtension(file);
            if (icons64.containsKey(ext)) {
                Image img = icons64.get(ext);
                if (img == null) {
                    try {
                        icons64.replace(ext, img = new Image("icon64/" + ext + ".png"));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
                image = img;
            }
        }
        return new ImageView(image);
    }

    public static ImageView getIcon(File file, int width) {
        ImageView imageView = getIcon(file);
        imageView.setFitWidth(width);
        imageView.setFitHeight(width);
        return imageView;
    }

    public static String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

    public static ImageView getIcon(Image image, int width) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(width);
        return imageView;
    }
}