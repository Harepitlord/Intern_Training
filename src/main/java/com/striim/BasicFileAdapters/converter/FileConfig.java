package com.striim.BasicFileAdapters.converter;

import org.springframework.stereotype.Component;


public class FileConfig {

    protected String filePath;
    protected String fileType;
    protected String delimiter;

    public String getFileType() {
        return fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String path) {
        filePath = path;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


}
