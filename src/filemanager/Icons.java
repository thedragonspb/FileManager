package filemanager;

import javafx.scene.image.Image;

import java.io.File;
import java.util.HashMap;

/**
 * Created by thedragonspb on 13.07.17.
 */
public class Icons {

    public static Image folderIcon32      = new Image("icon32/folder.png");
    public static Image emptyFolderIcon32 = new Image("icon32/empty_folder.png");
    public static Image folderIcon64      = new Image("icon64/folder.png");
    public static Image emptyFolderIcon64 = new Image("icon64/empty_folder.png");

    private static final HashMap<String, Image> icons32 = new HashMap<>();
    private static final HashMap<String, Image> icons64 = new HashMap<>();

    static {
        icons32.put("avi" , null);         icons32.put("css" , null);
        icons32.put("csv" , null);         icons32.put("dbf" , null);
        icons32.put("doc" , null);         icons32.put("exe" , null);
        icons32.put("html", null);         icons32.put("iso" , null);
        icons32.put("js"  , null);         icons32.put("jpg" , null);
        icons32.put("mp3" , null);         icons32.put("mp4" , null);
        icons32.put("pdf" , null);         icons32.put("png" , null);
        icons32.put("psd" , null);         icons32.put("rar" , null);
        icons32.put("txt" , null);         icons32.put("zip" , null);
        icons32.put("xls" , null);         icons32.put("xml" , null);
        icons32.put("zip" , null);         icons32.put("java", null);

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

    public static Image getIcon32(File file) {
        if (file.isDirectory()) {
            if (file.listFiles() != null && file.listFiles().length > 0) {
                return folderIcon32;
            } else {
                return emptyFolderIcon32;
            }
        } else {
            String ext = getFileExtension(file);
            if (icons32.containsKey(ext)) {
                Image img = icons32.get(ext);
                if (img == null) {
                    icons32.replace(ext, img = new Image("icon32/" + ext + ".png"));
                }
                return img;
            }
            return new Image("icon32/file.png");
        }
    }

    public static Image getIcon64(File file) {
        if (file.isDirectory()) {
            if (file.listFiles() != null && file.listFiles().length > 0) {
                return folderIcon64;
            } else {
                return emptyFolderIcon64;
            }
        } else {
            String ext = getFileExtension(file);
            if (icons64.containsKey(ext)) {
                Image img = icons64.get(ext);
                if (img == null) {
                    icons64.replace(ext, img = new Image("icon64/" + ext + ".png"));
                }
                return img;
            }
            return new Image("icon64/file.png");
        }
    }

    public static String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }
}