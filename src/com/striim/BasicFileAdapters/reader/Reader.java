package com.striim.BasicFileAdapters.reader;

import com.striim.BasicFileAdapters.database.DataRecord;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

abstract public class Reader {

    protected String filePath;
    protected FileReader fileReader;
    protected ArrayList<String> headers;
    protected Scanner scanner;
    protected String state;

    public Reader() {
        state = "Initial";
    }

    protected boolean prepareReader(String filePath) {
        this.filePath = filePath;
        try {
            fileReader = new FileReader(filePath);
            state = "File Connected";
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Error in opening the file");
            state = "File Not Found";
            return false;
        }
    }

    public String getState() {
        return state;
    }

    public ArrayList<String> getHeaders() {
        return headers;
    }

    abstract protected boolean prepareHeaders();
    abstract public Reader getInstance(String filePath);
    abstract public ArrayList<DataRecord> readFile();
    abstract public void initiate(Scanner scanner);
    protected abstract void finalize();

}
