package com.striim.BasicFileAdapters.database;

import java.util.ArrayList;
import java.util.stream.Collectors;

public abstract class StorageSpace {

    private static final ArrayList<String> availableStorageSpaces;

    static {
        availableStorageSpaces = new ArrayList<>();
        availableStorageSpaces.add("InMemoryStorage");
    }

    public static ArrayList<String> getAvailableStorageSpaces() {
        int i = 1;
        return (ArrayList<String>) availableStorageSpaces.stream().map(e -> String.format("%d.%s", i, e)).collect(Collectors.toList());
    }

    public abstract void addDataObjects(ArrayList<DataRecord> data);

    public abstract ArrayList<DataRecord> getDataObjArray();

    public abstract String getClassName();
}
