package download;

public enum DownloadStatus {
    none,
    analyzing,
    analyzed,
    downloading,
    complete,     // download successfully
    stop,         // stop download
    fail          // download fail
}