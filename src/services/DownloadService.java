package services;

import java.util.HashMap;

import network.FileDownloader;

public class DownloadService {
    HashMap<String, FileDownloader> runningTasks = new HashMap<String, FileDownloader>();

    public boolean addDownloadTask(String taskName, String remoteFileAddr, String localFileAddr)
    {
        try{
            FileDownloader fd = new FileDownloader(remoteFileAddr, localFileAddr);
            runningTasks.put(taskName, fd);
            System.out.println("task creat successfully");
            return true;
        }
        catch (Exception e)
        {
            System.out.println("task creat fail");
            return false;
        }
    }

    public boolean deleteDownloadTask(String taskName)
    {
        if (runningTasks.containsKey(taskName))
        {
            runningTasks.remove(taskName);
            return true;
        }
        return false;
    }

    public boolean startDownload(String taskName)
    {
        if (runningTasks.containsKey(taskName))
        {
            System.out.println("start to download");
            runningTasks.get(taskName).download();
            System.out.println("download success");
            return true;
        }
        System.out.println("download fail");
        return false;
    }
}