package utils;

import java.io.File;

public class FileHelper {
    public static boolean isDir(String path) {
        File f = new File(path);
        return f.exists() && f.isDirectory();
    }

    public static String getFileNameByPath(String path)
    {
        String fileName = path.substring(path.lastIndexOf("/")+1);
        if (fileName.length() == 0)
        {
            fileName = "undefined.html";
        }
        return fileName;
    }
}