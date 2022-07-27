package com.striim.BasicFileAdapters.writer;

import com.striim.BasicFileAdapters.UserInterface.UserInterface;
import com.striim.BasicFileAdapters.converter.FileConfig;
import com.striim.BasicFileAdapters.database.StorageSpace;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

public abstract class Writer {

    protected FileWriter fileWriter;
    protected String state;
    protected UserInterface userInterface;
    protected FileConfig fileConfig;

    private static final ArrayList<String> availableWriters;

    static{
        availableWriters=new ArrayList<>();
        availableWriters.add("JSONWRITER");
    }

    public static ArrayList<String> getAvailableWriters(){
        return availableWriters;
    }

    protected boolean prepareWriter() {
        try {
            File file = new File(fileConfig.getFilePath());
            fileWriter = new FileWriter(file, true);
            state = "File Connected";
            return true;
        } catch (IOException ex) {
            state = "File Error";
            return false;
        }
    }

    public static boolean isAvailable(String path){
        return getAvailableWriters().contains(path.substring(path.lastIndexOf(".")+1,path.length()).toUpperCase()+"WRITER");
    }

    public void setFileConfig(FileConfig fileConfig) {
        this.fileConfig = fileConfig;
    }

    public abstract String getName();

    public abstract void writeFile(UserInterface userInterface, StorageSpace database, ExecutorService executorService);

    public abstract Writer getInstance(FileConfig fileConfig);

}
