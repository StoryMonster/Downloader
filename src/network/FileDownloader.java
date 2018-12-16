package network;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import surface.DownloadTask;
import network.DownloadStatus;


class DownloadThread extends Thread {
    String remoteAddr;
    URL remoteUrl;
    String fileToSave;
    long fileSize;
    long receivedBytesAmount = 0;
    int receiveBufferSize = 1024;
    DownloadTask dlTask;

    public DownloadThread(String remoteAddr, URL remoteUrl, String fileToSave, long fileSize, DownloadTask dlTask)
    {
        super(remoteAddr);
        this.remoteAddr = remoteAddr;
        this.remoteUrl = remoteUrl;
        this.fileToSave = fileToSave;
        this.fileSize = fileSize;
        this.dlTask = dlTask;
    }

    public void run()
    {
        try {
            dlTask.updateStatus(DownloadStatus.downloading);
            BufferedInputStream in = new BufferedInputStream(remoteUrl.openStream());
            FileOutputStream outStream = new FileOutputStream(fileToSave);
            byte data[] = new byte[receiveBufferSize];
            int byteContent;
            while (dlTask.getStatus() == DownloadStatus.downloading && (byteContent = in.read(data, 0, receiveBufferSize)) != -1) {
                outStream.write(data, 0, byteContent);
                receivedBytesAmount = receivedBytesAmount + byteContent;
                System.out.println(receivedBytesAmount+"/"+fileSize);
            }
            in.close();
            outStream.close();
            if (dlTask.getStatus() == DownloadStatus.downloading) {
                System.out.println("download " + remoteAddr + " successfully");
                dlTask.updateStatus(DownloadStatus.complete);
            } else {
                System.out.println("download " + remoteAddr + " meet troubles");
            }
        } catch (Exception e) {
            System.out.println("download " + remoteAddr + " fail");
            dlTask.updateStatus(DownloadStatus.fail);
        }
    }
}

public class FileDownloader {
    URL remoteUrl = null;
    String fileToSave;
    String remoteFileAddr;
    long fileSize = -1;
    DownloadThread dlThread;
    DownloadTask dlTask;

    public FileDownloader(String remoteFileAddress, String fileToSave, DownloadTask dlTask)
    {
        try
        {
            System.out.println("Creating downloader");
            this.remoteUrl = new URL(remoteFileAddress);
            this.fileToSave = fileToSave;
            this.remoteFileAddr = remoteFileAddress;
            this.dlTask = dlTask;
        }
        catch (Exception e)
        {
            dlTask.updateStatus(DownloadStatus.fail);
            throw new RuntimeException(e);
        }
    }

    public void analyzeRemoteFile()
    {
        dlTask.updateStatus(DownloadStatus.analyzing);
        try {
            HttpURLConnection conn = (HttpURLConnection) remoteUrl.openConnection();
            conn.setRequestMethod("GET");
            fileSize = conn.getContentLengthLong();
            conn.disconnect();
            System.out.println("remote file size: " + fileSize + "bytes");
            dlTask.updateStatus(DownloadStatus.analyzed);
        } catch (Exception e) {
            dlTask.updateStatus(DownloadStatus.analyzed);
            System.out.println("Analyze remote file fail");
            throw new RuntimeException("Analyze remote file fail");
        }
    }

    public long getRemoteFileSize() {
        return fileSize;
    }

    public void download()
    {
        dlThread = new DownloadThread(remoteFileAddr, remoteUrl, fileToSave, fileSize, dlTask);
        dlThread.start();
    }

    public void stopDownload()
    {
        try {
            if (dlTask.getStatus() == DownloadStatus.downloading) {
                dlTask.setStatus(DownloadStatus.stop);
                dlThread.join(5000);
            }
        } catch(Exception e) {
            System.out.println("Cannot stop download thread");
        }
    }
}
