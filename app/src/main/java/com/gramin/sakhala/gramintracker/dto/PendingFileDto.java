package com.gramin.sakhala.gramintracker.dto;

/**
 * Created by atulsakhala on 12/08/18.
 */

public class PendingFileDto {
    String filePath;
    String fileName;

    public PendingFileDto(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
