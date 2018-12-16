package utils;


public class StreamLengthHelper {
    static public String humanReadable(long streamBytes) {
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
                }
                else {
                    return String.format("%.2f%s", streamBytes*1.0/bytes[i-1], units[i]);
                }
            }
        }
        int index = bytes.length-1;
        return String.format("%.2f%s", streamBytes*1.0/bytes[index], units[index]);
    }
}