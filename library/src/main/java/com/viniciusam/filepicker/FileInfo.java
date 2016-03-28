package com.viniciusam.filepicker;

import java.io.File;

/**
 * Created by Vinicius Avellar on 21/03/2016.
 */
public class FileInfo implements Comparable<FileInfo> {

    private String filePath;
    private String fileName;
    private boolean isDirectory;
    private long fileSize;
    private long lastModified;

    public static FileInfo newParentLink(File parentFile) {
        FileInfo parentLink = new FileInfo(parentFile);
        parentLink.fileName = "..";
        return parentLink;
    }

    public FileInfo(File file) {
        this.filePath = file.getAbsolutePath();
        this.fileName = file.getName();
        this.isDirectory = file.isDirectory();
        this.fileSize = file.length();
        this.lastModified = file.lastModified();
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public long getFileSize() {
        return fileSize;
    }

    public long getLastModified() {
        return lastModified;
    }

    public boolean isParentLink() {
        return this.fileName.equals("..");
    }

    @Override
    public int compareTo(FileInfo another) {
        if (another.isDirectory() && !this.isDirectory()) {
            return 1;
        }

        if (!another.isDirectory() && this.isDirectory()) {
            return -1;
        }

        return this.getFileName().compareTo(another.getFileName());
    }
}
