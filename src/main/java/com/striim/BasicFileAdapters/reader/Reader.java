package com.striim.BasicFileAdapters.reader;

import com.striim.BasicFileAdapters.UserInterface.UserInterface;
import com.striim.BasicFileAdapters.converter.FileConfig;
import com.striim.BasicFileAdapters.database.DataRecord;
import lombok.extern.slf4j.XSlf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

@XSlf4j(topic = "General")
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

            log.info("{} -- {} -- The file has been connected",fileConfig.getType(),fileConfig.getFilePath());

            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Error in opening the file");
            state = "File Not Found";

            log.error("{} -- {} -- Error in opening the file.", fileConfig.getType(), fileConfig.getFilePath());

            return false;
        }
    }

    public static boolean isAvailable(String path) {
        return getAvailableReaders().contains(path.substring(path.lastIndexOf(".") + 1).toUpperCase() + "READER");
    }

    protected void errorHandling() {
        switch (state) {
            case "File Not Found":
                System.out.println("There is no such file in the given path, Re-enter new File Path");

                log.warn("{} -- {} -- The file is not found", fileConfig.getType(), fileConfig.getFilePath());
                break;

            case "Data Not Found":
                System.out.println("The given file doesn't contain a record");
                this.fileReader = null;

                log.warn("{} -- {} -- The file is empty", fileConfig.getType(), fileConfig.getFilePath());
                break;

            case "Read Error":
                System.out.println("The given file not readable, provide filepath for the new file");
                this.fileReader = null;

                log.warn("{} -- {} -- Unable to access the file. ", fileConfig.getType(), fileConfig.getFilePath());
                break;
        }
    }

    public void setFileConfig(FileConfig fileConfig) {
        this.fileConfig = fileConfig;
    }

    abstract protected boolean prepareHeaders();

    abstract public ArrayList<DataRecord> readFile();

    public String getName() {
        return String.format("%s -- %s", fileConfig.getType(), fileConfig.getFilePath());
    }

    public String getFilePath() {
        return fileConfig.getFilePath();
    }

    protected abstract void finalize();

}
