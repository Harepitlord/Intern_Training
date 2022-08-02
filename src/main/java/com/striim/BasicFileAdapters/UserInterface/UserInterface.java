package com.striim.BasicFileAdapters.UserInterface;

import com.striim.BasicFileAdapters.converter.FileConfig;

import java.util.ArrayList;

public abstract class UserInterface {

    private static final ArrayList<String> availableUserInterfaces;

    static {
        availableUserInterfaces=new ArrayList<>();
        availableUserInterfaces.add("1.Console Interface");
        availableUserInterfaces.add("2.XML Interface");
    }

    public static ArrayList<String> getAvailableUserInterfaces(){
        return availableUserInterfaces;
    }

    public abstract ArrayList<FileConfig> getReaderFileConfigs();

    public abstract ArrayList<FileConfig> getWriterFileConfigs();

    public abstract FileConfig getReaderFileConfig();

    public abstract FileConfig getWriterFileConfig();

    public abstract void generateQueries(FileConfig fileConfig, ArrayList<String> keySet);

    public abstract void fetchColumns(FileConfig fileConfig, ArrayList<String> keyset);

    public abstract String getStorageType();

    public abstract String getClassName();

    public abstract void print(String msg);

}
