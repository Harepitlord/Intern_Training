package com.striim.BasicFileAdapters.writer;

import com.striim.BasicFileAdapters.database.*;
import java.io.*;
import java.util.*;

public abstract class Writer {

    protected FileWriter fileWriter;
    protected String state;

    protected boolean prepareWriter(String filepath){
        try {
            File file = new File(filepath);
            file.createNewFile();
            fileWriter=new FileWriter(file,true);
            state = "File Connected";
            return true;
        }
        catch(IOException ex){
            state="File Error";
            return false;
        }
    }

    public abstract String getName();
    public abstract void writeFile(Scanner scanner,InMemoryDatabase database);
    public abstract Writer getInstance(String filePath);

}
