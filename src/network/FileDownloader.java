package network;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import surface.TaskLabel;
import network.DownloadStatus;


class DownloadThread extends Thread {
    String remoteAddr;
    URL remoteUrl;
    String fileToSave;
    long fileSize;
    long receivedBytesAmount = 0;
    int receiveBufferSize = 1024;
    TaskLabel label;

    public DownloadThread(String remoteAddr, URL remoteUrl, String fileToSave, long fileSize, TaskLabel label)
    {
        super(remoteAddr);
        this.remoteAddr = remoteAddr;
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
            byte data[] = new byte[receiveBufferSize];
            int byteContent;
            while ((byteContent = in.read(data, 0, receiveBufferSize)) != -1) {
                outStream.write(data, 0, byteContent);
                receivedBytesAmount = receivedBytesAmount + byteContent;
                System.out.println(receivedBytesAmount+"/"+fileSize);
            }
            in.close();
            outStream.close();
            System.out.println("download " + remoteAddr + " successfully");
            label.updateStatus(DownloadStatus.complete);
        } catch (Exception e) {
            System.out.println("download " + remoteAddr + " fail");
            label.updateStatus(DownloadStatus.fail);
        }
    }
}

public class FileDownloader {
    URL remoteUrl = null;
    String fileToSave;
    String remoteFileAddr;
    long fileSize = -1;
    DownloadThread dlThread;
    TaskLabel label;

    public FileDownloader(String remoteFileAddress, String fileToSave, TaskLabel label)
    {
        try
        {
            System.out.println("Creating downloader");
            this.remoteUrl = new URL(remoteFileAddress);
            this.fileToSave = fileToSave;
            this.remoteFileAddr = remoteFileAddress;
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
            label.updateStatus(DownloadStatus.analyzed);
        } catch (Exception e) {
            label.updateStatus(DownloadStatus.analyzed);
            System.out.println("Analyze remote file fail");
            throw new RuntimeException("Analyze remote file fail");
        }
    }

    public long getRemoteFileSize() {
        return fileSize;
    }

    public void download()
    {
        dlThread = new DownloadThread(remoteFileAddr, remoteUrl, fileToSave, fileSize, label);
        dlThread.start();
    }
}
