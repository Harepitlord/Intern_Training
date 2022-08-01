package com.striim.BasicFileAdapters.UserInterface;

import com.striim.BasicFileAdapters.converter.FileConfig;
import com.striim.BasicFileAdapters.database.DataRecord;

import java.util.ArrayList;
import java.util.function.Predicate;

public abstract class UserInterface {

    private static final ArrayList<String> availableUserInterfaces;

    static {
        availableUserInterfaces=new ArrayList<>();
        availableUserInterfaces.add("1.Console Interface");
    }

    public static ArrayList<String> getAvailableUserInterfaces(){
        return availableUserInterfaces;
    }


    public abstract UserInterface getInterface();

    public abstract void prepareReaderFileConfigs();

    public abstract void prepareWriterFileConfigs();

    public abstract ArrayList<FileConfig> getReaderFileConfigs();

    public abstract ArrayList<FileConfig> getWriterFileConfigs();

    public abstract FileConfig getReaderFileConfig();

    public abstract FileConfig getWriterFileConfig();

    public abstract Predicate<DataRecord> generateQueries(ArrayList<String> keySet);

    public abstract ArrayList<String> fetchColumns(ArrayList<String> keyset);

    public abstract String getStorageType();

    public abstract String getClassName();

    public abstract void print(String msg);

}
