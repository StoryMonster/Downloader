cmake_minimum_required(VERSION 3.1)

set(CMAKE_MAKE_PROGRAM "MinGW Makefiles")
set(CMAKE_JAVA_COMPILER_ENV_VAR $ENV{JAVA_HOME})
set(CMAKE_JAVA_COMPILER $ENV{JAVA_HOME}/bin/javac.exe)

project(Downloader VERSION 1.0
                   DESCRIPTION "This is a tool to download files"
                   LANGUAGES JAVA)

find_package(Java REQUIRED)
include(UseJava)

set(CMAKE_JAVA_COMPILE_FLAGS -Xlint:unchecked -d build)
add_jar(Downloader
        src/Downloader.java
        src/surface/MainSurface.java
        src/surface/RunningTaskList.java
        src/surface/DownloadTask.java
        src/surface/TaskListModel.java
        src/surface/RunningTaskListMouseAdapter.java
        src/network/FileDownloader.java
        src/network/DownloadStatus.java
        src/utils/ConfigHelper.java
        src/utils/FileHelper.java
        src/utils/StreamLengthHelper.java
        src/context/MainSurfaceContext.java)
