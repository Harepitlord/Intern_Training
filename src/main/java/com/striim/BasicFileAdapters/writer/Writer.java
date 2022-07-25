package com.striim.BasicFileAdapters.writer;

import com.striim.BasicFileAdapters.UserInterface.UserInterface;
import com.striim.BasicFileAdapters.converter.FileConfig;
import com.striim.BasicFileAdapters.database.StorageSpace;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

public abstract class Writer {

    protected FileWriter fileWriter;
    protected String state;
    protected UserInterface userInterface;
    protected FileConfig fileConfig;

    protected boolean prepareWriter(FileConfig fileConfig) {
        try {
            File file = new File(fileConfig.getFilePath());
            boolean newFile = file.createNewFile();
            fileWriter = new FileWriter(file, true);
            state = "File Connected";
            return true;
        } catch (IOException ex) {
            state = "File Error";
            return false;
        }
    }

    public void setFileConfig(FileConfig fileConfig) {
        this.fileConfig = fileConfig;
    }

    public abstract String getName();

    public abstract void writeFile(UserInterface userInterface, StorageSpace database, ExecutorService executorService);

    public abstract Writer getInstance(FileConfig fileConfig);

}
