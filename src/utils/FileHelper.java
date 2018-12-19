package utils;

import java.io.File;

public class FileHelper {
    private static int fileId = 0;
    public static boolean isDir(String path) {
        File f = new File(path);
        return f.exists() && f.isDirectory();
    }

    public static String getFileNameInUrl(String url)
    {
        String fileName = url.split("\\?")[0];
        fileName = fileName.substring(fileName.lastIndexOf("/")+1);
        if (fileName.length() == 0)
        {
            ++fileId;
            fileName = "unknown file " + fileId;
        }
        return fileName;
    }
}