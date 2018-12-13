package network;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import network.DownloadStatusRef;


class DownloadThread extends Thread {
    String name;
    URL remoteUrl;
    String fileToSave;
    long fileSize;
    DownloadStatusRef status;
    long receivedBytesAmount = 0;

    public DownloadThread(String name, URL remoteUrl, String fileToSave, long fileSize, DownloadStatusRef status)
    {
        super(name);
        this.name = name;
        this.remoteUrl = remoteUrl;
        this.fileToSave = fileToSave;
        this.fileSize = fileSize;
        this.status = status;
    }

    public void run()
    {
        try {
            status.value = DownloadStatus.downloading;
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
            status.value = DownloadStatus.complete;
        } catch (Exception e) {
            System.out.println("Thread " + name + " abort");
            status.value = DownloadStatus.fail;
        }
    }
}

public class FileDownloader {
    URL remoteUrl = null;
    String fileToSave;
    long fileSize = 0;
    DownloadStatusRef status;
    DownloadThread dlThread;

    public FileDownloader(String remoteFileAddress, String fileToSave, DownloadStatusRef status)
    {
        try
        {
            System.out.println("Creating downloader");
            this.status = status;
            this.status.setValue(DownloadStatus.analyzing);
            remoteUrl = new URL(remoteFileAddress);
            this.fileToSave = fileToSave;
            HttpURLConnection conn = (HttpURLConnection) remoteUrl.openConnection();
            conn.setRequestMethod("GET");
            fileSize = conn.getContentLengthLong();
            conn.disconnect();
            System.out.println("remote file size: " + fileSize + "bytes");
            Thread.sleep(1000);
        }
        catch (Exception e)
        {
            status.value = DownloadStatus.fail;
            throw new RuntimeException(e);
        }
    }

    public long getRemoteFileSize() {
        return fileSize;
    }

    public void download()
    {
        dlThread = new DownloadThread("name", remoteUrl, fileToSave, fileSize, status);
        dlThread.start();
    }

    public boolean downloadSuccess()
    {
        return status.value == DownloadStatus.complete;
    }

    public DownloadStatusRef getStatus()
    {
        return status;
    }
}
