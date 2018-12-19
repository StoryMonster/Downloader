package utils;


public class HumanReadHelper {
    static public String readFileSize(long streamBytes) {
        if (streamBytes < 0) {
            return String.format("%d", streamBytes);
        }
        String[] units = {"", "B", "KB", "MB", "GB", "TB"};
        long[] bytes = {1,
                        1024,
                        1024*1024,
                        1024*1024*1024,
                        1024*1024*1024*1024,
                        1024*1024*1024*1024*1024};
        for (int i = 1; i < bytes.length; ++i)
        {
            if (streamBytes < bytes[i]) {
                if (i == 1) {
                    return String.format("%d%s", streamBytes/bytes[i-1], units[i]);
                } else {
                    return String.format("%.2f%s", streamBytes*1.0/bytes[i-1], units[i]);
                }
            }
        }
        int index = bytes.length-1;
        return String.format("%.2f%s", streamBytes*1.0/bytes[index], units[index]);
    }

    /*
     * String readDownloadSpeed(long bytes, long interval)
     * parameters:
     *     bytes: increased bytes amount
     *     interval: the collection duration, milliseconds
     * Return value:
     *     the download speed in human readable string, bytes/second
     */
    static public String readDownloadSpeed(long bytes, long interval)
    {
        long bytesPerSecend = 1000 * bytes / interval;
        return String.format("%s/s", readFileSize(bytesPerSecend));
    }

    static public String readDownloadProgress(double rate)
    {
        return String.format("%.2f%%", rate * 100);
    }
}