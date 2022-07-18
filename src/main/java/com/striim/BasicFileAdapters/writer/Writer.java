package com.striim.BasicFileAdapters.writer;

import com.striim.BasicFileAdapters.database.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;

public abstract class Writer {

    protected FileWriter fileWriter;
    protected String state;
    protected Scanner scanner;

    protected boolean prepareWriter(String filepath){
        try {
            File file = new File(filepath);
            boolean newFile = file.createNewFile();
            fileWriter=new FileWriter(file,true);
            state = "File Connected";
            return true;
        }
        catch(IOException ex){
            state="File Error";
            return getFilePath();
        }
    }

    private boolean getFilePath() {
        System.out.println("Enter proper File path for the csv file: ");
        String temp = scanner.nextLine();
        if(temp.length()>0)
            return prepareWriter(temp);
        else
            return getFilePath();
    }

    public abstract String getName();
    public abstract void writeFile(Scanner scanner, InMemoryDatabase database, ExecutorService executorService);
    public abstract Writer getInstance(String filePath);

}
