package surface;

import javax.swing.JLabel;

import network.DownloadStatus;
import network.FileDownloader;
import utils.StreamLengthHelper;

public class TaskLabel extends JLabel{
    String taskName;
    String srcAddr;
    String localAddr;
    DownloadStatus status = DownloadStatus.none;
    FileDownloader downloader;

    public TaskLabel(String taskName, String srcAddr, String localAddr)
    {
        this.taskName = taskName;
        this.srcAddr = srcAddr;
        this.localAddr = localAddr;
        this.downloader = new FileDownloader(srcAddr, localAddr, this);
    }

    public String getName()
    {
        return taskName;
    }

    public void analyzeRemoteFile()
    {
        downloader.analyzeRemoteFile();
    }

    public boolean isRemoteFileAccessable()
    {
        return downloader.getRemoteFileSize() != -1;
    }

    public void startDownload()
    {
        downloader.download();
    }

    public void updateStatus(DownloadStatus status)
    {
        this.status = status;
        this.updateContent();
    }

    private String getStatusColor()
    {
        switch (status) {
            case downloading : return "Green";
            case analyzing: return "Black";
            case complete: return "Green";
            case fail: return "Red";
            default: return "White";
        }
    }

    private void updateContent()
    {
        setText(this.toString());
    }

    @Override
    public String toString() {
        String statusColor = getStatusColor();
        String content;
        if (status == DownloadStatus.analyzed) {
            content = String.format(
                "<h4>%s</h4>" +
                "%s", taskName, StreamLengthHelper.humanReadable(downloader.getRemoteFileSize()));
        }
        else {
            content = String.format(
                "<h4>%s<br>" +
                "<font color=%s>%s</font>", this.taskName, statusColor, status.toString());
        }
        return "<html>" + content + "</html>";
    }
}
