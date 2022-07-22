package com.striim.BasicFileAdapters.UserInterface;

import com.striim.BasicFileAdapters.converter.FileConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

@Component
public class ConsoleInterface implements UserInterface {

    private Scanner scanner;
    private ArrayList<FileConfig> files;

    @Autowired
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    @Bean
    public UserInterface getInterface() {
        return new ConsoleInterface();
    }

    public void fileSpecificConfig(FileConfig fileConfig) {
        if (fileConfig.getFileType().equals("CSV")) {
            System.out.println("Enter the delimiter : (default => ',')");
            char delim = ',';
            String val = scanner.nextLine();
            if (val.length() > 1)
                delim = val.trim().charAt(0);
            fileConfig.setDelimiter(String.valueOf(delim));
        }
    }

    public boolean filePathInput(FileConfig fileConfig) {

        String path;
        System.out.println("Enter The file Path: ");
        boolean next = true;
        while (true) {
            path = scanner.nextLine().trim();
            if (path.length() == 0 || path.equals(";")) {
                if (files.size() > 0)
                    next = false;
                path = path.substring(0, path.length() - 1);
            }
            File f = new File(path);
            if (f.isFile()) {
                fileConfig.setFilePath(path);
                setFileTypeType(fileConfig);
                break;
            }
            System.out.println("Enter proper file path: ");
        }
        fileSpecificConfig(fileConfig);
        return next;
    }

    protected void setFileTypeType(FileConfig fileConfig) {
        int index = fileConfig.getFilePath().lastIndexOf(".");
        fileConfig.setFileType(fileConfig.getFilePath().substring(index).toUpperCase());
    }

    public void prepareFileConfigs() {
        files = new ArrayList<>();
        boolean next = true;
        while (next) {
            FileConfig fileConfig = new FileConfig();

            next = filePathInput(fileConfig);

            files.add(fileConfig);
        }
    }

    public ArrayList<FileConfig> getFileConfigs() {
        return files;
    }
}
