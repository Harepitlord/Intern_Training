package com.striim.BasicFileAdapters.database;

import java.util.ArrayList;

public abstract class  StorageSpace {

    private static final ArrayList<String> availableStorageSpaces;

    static {
        availableStorageSpaces=new ArrayList<>();
        availableStorageSpaces.add("1.InMemoryStorage");
    }

    public static ArrayList<String> getAvailableStorageSpaces(){
        return availableStorageSpaces;
    }

    public abstract void addDataObjects(ArrayList<DataRecord> data);

    public abstract ArrayList<DataRecord> getDataObjArray();

    public abstract String getClassName();
}
