package com.striim.BasicFileAdapters.converter;

import java.io.File;
import java.util.Scanner;

public class FileConfig {

    protected String filePath;
    protected String fileType;
    protected String type;
    protected String delimiter;
    protected Scanner scanner;

    public FileConfig(Scanner scanner) {
        this.scanner = scanner;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getType() {
        return type;
    }

    public void fileSpecificConfig() {
        if(fileType.equals("CSV")) {
            System.out.println("Enter the delimiter : (default => ',')");
            char delim = ',';
            String val = scanner.nextLine();
            if(val.length() > 1)
                delim = val.trim().charAt(0);
            delimiter = String.valueOf(delim);
        }
    }

    public void filePathInput() {
        String path;
        System.out.println("Enter The file Path: ");
        while(true) {
            path = scanner.nextLine().trim();
            File f = new File(path);
            if(f.isFile()) {
                filePath = path;
                setFileTypeType();
                break;
            }
            System.out.println("Enter proper file path: ");
        }
        fileSpecificConfig();
    }

    protected void setFileTypeType() {
        int index = filePath.lastIndexOf(".");
        fileType = filePath.substring(index).toUpperCase();
    }
}
