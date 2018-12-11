package network;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.IOException;

public class FileDownloader {
    URL remoteUrl = null;
    String fileToSave;
    long receivedBytesAmount = 0;
    long totalBytesOfRemoteFile = 0;

    public FileDownloader(String remoteFileAddress, String fileToSave)
    {
        try
        {
            remoteUrl = new URL(remoteFileAddress);
            this.fileToSave = fileToSave;
            HttpURLConnection conn = (HttpURLConnection) remoteUrl.openConnection();
            conn.setRequestMethod("GET");
            totalBytesOfRemoteFile = conn.getContentLengthLong();
            conn.disconnect();
            System.out.println("remote file size: " + totalBytesOfRemoteFile + "bytes");
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public long getRemoteFileSize() {
        return totalBytesOfRemoteFile;
    }

    public long download()
    {
        try {
            BufferedInputStream in = new BufferedInputStream(remoteUrl.openStream());
            FileOutputStream outStream = new FileOutputStream(fileToSave);
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = in.read(data, 0, 1024)) != -1) {
                outStream.write(data, 0, byteContent);
                receivedBytesAmount = receivedBytesAmount + byteContent;
                System.out.println(receivedBytesAmount+"/"+totalBytesOfRemoteFile);
            }
            in.close();
            outStream.close();
            return receivedBytesAmount;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }   
    }

    public boolean downloadSuccess()
    {
        return receivedBytesAmount == totalBytesOfRemoteFile;
    }
}
