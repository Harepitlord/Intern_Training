package com.striim.BasicFileAdapters.database;

import java.util.ArrayList;

// This class acts as Java-based imitation of database
public class InMemoryDatabase {

    // This collection stores the records
    private final ArrayList<DataRecord> dataObjArray =new ArrayList<>();

    // Getter function
    public ArrayList<DataRecord> getDataObjArray() {
        return dataObjArray;
    }

    // Utility Functions
    public void addDataObjects(ArrayList<DataRecord> data) {
        this.dataObjArray.addAll(data);
    }

    public int size() {
        return dataObjArray.size();
    }
}
