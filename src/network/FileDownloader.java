package network;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import surface.TaskLabel;
import network.DownloadStatus;


class DownloadThread extends Thread {
    String name;
    URL remoteUrl;
    String fileToSave;
    long fileSize;
    long receivedBytesAmount = 0;
    TaskLabel label;

    public DownloadThread(String name, URL remoteUrl, String fileToSave, long fileSize, TaskLabel label)
    {
        super(name);
        this.name = name;
        this.remoteUrl = remoteUrl;
        this.fileToSave = fileToSave;
        this.fileSize = fileSize;
        this.label = label;
    }

    public void run()
    {
        try {
            label.updateStatus(DownloadStatus.downloading);
            BufferedInputStream in = new BufferedInputStream(remoteUrl.openStream());
            FileOutputStream outStream = new FileOutputStream(fileToSave);
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = in.read(data, 0, 1024)) != -1) {
                outStream.write(data, 0, byteContent);
                Thread.sleep(1000);
                receivedBytesAmount = receivedBytesAmount + byteContent;
                System.out.println(receivedBytesAmount+"/"+fileSize);
            }
            in.close();
            outStream.close();
            System.out.println("Thread " + name + " done");
            label.updateStatus(DownloadStatus.complete);
        } catch (Exception e) {
            System.out.println("Thread " + name + " abort");
            label.updateStatus(DownloadStatus.fail);
        }
    }
}

public class FileDownloader {
    URL remoteUrl = null;
    String fileToSave;
    long fileSize = 0;
    DownloadThread dlThread;
    TaskLabel label;

    public FileDownloader(String remoteFileAddress, String fileToSave, TaskLabel label)
    {
        try
        {
            System.out.println("Creating downloader");
            this.remoteUrl = new URL(remoteFileAddress);
            this.fileToSave = fileToSave;
            this.label = label;
        }
        catch (Exception e)
        {
            label.updateStatus(DownloadStatus.fail);
            throw new RuntimeException(e);
        }
    }

    public void analyzeRemoteFile()
    {
        label.updateStatus(DownloadStatus.analyzing);
        try {
            HttpURLConnection conn = (HttpURLConnection) remoteUrl.openConnection();
            conn.setRequestMethod("GET");
            fileSize = conn.getContentLengthLong();
            conn.disconnect();
            System.out.println("remote file size: " + fileSize + "bytes");
        } catch (Exception e) {
            label.updateStatus(DownloadStatus.fail);
            System.out.println("Analyze remote file fail");
            throw new RuntimeException("Analyze remote file fail");
        }
    }

    public long getRemoteFileSize() {
        return fileSize;
    }

    public void download()
    {
        dlThread = new DownloadThread("name", remoteUrl, fileToSave, fileSize, label);
        dlThread.start();
    }
}
