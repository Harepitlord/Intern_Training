package com.striim.BasicFileAdapters.writer;

import com.striim.BasicFileAdapters.database.*;
import java.io.*;
import java.util.*;

public abstract class Writer {

    File file;
    FileWriter fileWriter;

    void prepareWriter(String filepath){
        file=new File(filepath);
        try {
            if(file.createNewFile())
                fileWriter=new FileWriter(file,true);
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public abstract void writeFile(String filepath,ArrayList<DataRecord> toWriteResultSet);
    public abstract Writer getInstance();

}
