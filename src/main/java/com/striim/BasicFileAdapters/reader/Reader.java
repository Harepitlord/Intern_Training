package com.striim.BasicFileAdapters.reader;

import com.striim.BasicFileAdapters.UserInterface.UserInterface;
import com.striim.BasicFileAdapters.converter.FileConfig;
import com.striim.BasicFileAdapters.database.DataRecord;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

@Slf4j
abstract public class Reader {

    protected FileConfig fileConfig;
    protected FileReader fileReader;
    protected ArrayList<String> headers;
    protected UserInterface userInterface;
    protected String state;

    public Reader() {
        state = "Initial";
    }

    private static final ArrayList<String> availableReaders;

    static{
        availableReaders=new ArrayList<>();
        availableReaders.add("CSVREADER");
    }

    public static ArrayList<String> getAvailableReaders(){
        return availableReaders;
    }

    protected boolean prepareReader(FileConfig fileConfig) {
        this.fileConfig = fileConfig;
        try {
            fileReader = new FileReader(fileConfig.getFilePath());
            state = "File Connected";
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Error in opening the file");
            log.error("{} -- {} -- Error in opening the file.", fileConfig.getType(), fileConfig.getFilePath());
            state = "File Not Found";
            return false;
        }
    }

    public static boolean isAvailable(String path){
        return getAvailableReaders().contains(path.substring(path.lastIndexOf(".")+1).toUpperCase()+"READER");
    }

    public void setFileConfig(FileConfig fileConfig) {
        this.fileConfig = fileConfig;
    }

    public String getState() {
        return state;
    }

    public ArrayList<String> getHeaders() {
        return headers;
    }

    abstract protected boolean prepareHeaders();

    abstract public ArrayList<DataRecord> readFile();

    public String getName() {
        return String.format("%s -- %s", fileConfig.getType(), fileConfig.getFilePath());
    }

    protected abstract void finalize();

}
