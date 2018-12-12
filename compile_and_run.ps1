javac -Xlint:unchecked  src/Downloader.java src/surface/MainSurface.java src/surface/RunningTaskList.java src/services/DownloadService.java src/network/FileDownloader.java -d build/
cd build
java Downloader
cd ..