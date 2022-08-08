package com.striim.BasicFileAdapters.converter;

public class FileConfig {

    protected String filePath;
    protected String fileType;
    protected String delimiter;
    protected String type;

    public String getFileType() {
        return fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public String getType() {
        return type;
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

    public void setType(String type) {
        this.type = type.toUpperCase();
    }
}
