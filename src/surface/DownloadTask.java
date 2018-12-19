package surface;

import javax.swing.JLabel;

import download.DownloadStatus;
import download.FileDownloader;
import utils.HumanReadHelper;

public class DownloadTask extends JLabel{
    String taskName;
    String srcAddr;
    String localAddr;
    DownloadStatus status = DownloadStatus.none;
    FileDownloader downloader;

    public DownloadTask(String taskName, String srcAddr, String localAddr)
    {
        this.taskName = taskName;
        this.srcAddr = srcAddr;
        this.localAddr = localAddr;
        updateContent();
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

    public void stopDownload()
    {
        downloader.stopDownload();
    }

    public void updateStatus(DownloadStatus status)
    {
        this.setStatus(status);
        this.updateContent();
    }

    public DownloadStatus getStatus()
    {
        return status;
    }

    public void setStatus(DownloadStatus status)
    {
        this.status = status;
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

    public void updateContent()
    {
        String statusColor = getStatusColor();
        String content;
        if (status == DownloadStatus.analyzed) {
            content = String.format(
                "<h4>%s</h4>" +
                "%s", taskName, HumanReadHelper.readFileSize(downloader.getRemoteFileSize()));
        } else if (status == DownloadStatus.downloading){
            content = String.format(
                "<h4>%s</h4>" +
                "downloading  speed: %s    progress: %s  total size: %s",
                taskName,
                HumanReadHelper.readDownloadSpeed(downloader.getNewlyDownloadedSize(), 1000),
                HumanReadHelper.readDownloadProgress(downloader.getDownloadedRate()),
                HumanReadHelper.readFileSize(downloader.getRemoteFileSize()));
        } else {
            content = String.format(
                "<h4>%s<br>" +
                "<font color=%s>%s</font>", this.taskName, statusColor, status.toString());
        }
        setText("<html>" + content + "</html>");
    }

    @Override
    public String toString() {
        return getText();
    }
}
