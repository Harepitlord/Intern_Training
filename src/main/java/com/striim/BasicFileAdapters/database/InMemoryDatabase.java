package com.striim.BasicFileAdapters.database;

import java.util.ArrayList;

// This class acts as Java-based imitation of database
public class InMemoryDatabase implements StorageSpace {

    private final ArrayList<DataRecord> dataObjArray = new ArrayList<>();

    public ArrayList<DataRecord> getDataObjArray() {
        return dataObjArray;
    }

    public void addDataObjects(ArrayList<DataRecord> data) {
        this.dataObjArray.addAll(data);
    }

    public int size() {
        return dataObjArray.size();
    }
}
