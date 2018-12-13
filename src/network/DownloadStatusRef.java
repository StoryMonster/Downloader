package network;

import network.DownloadStatus;

public class DownloadStatusRef {
    public DownloadStatus value;

    public DownloadStatusRef()
    {
        this.value = DownloadStatus.analyzing;
    }

    public void setValue(DownloadStatus value) {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return value.name();
    }
}