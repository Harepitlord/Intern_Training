package com.striim.BasicFileAdapters.database;

import java.util.ArrayList;

// This class acts as Java-based imitation of database
public class InMemoryDatabase implements Database{

    private final ArrayList<DataRecord> dataObjArray =new ArrayList<>();

    @Override
    public String getType() {
        return "InMemoryDatabase";
    }

    public ArrayList<DataRecord> getDataObjArray(String topicId) {
        return dataObjArray;
    }

    public void addDataObjects(ArrayList<DataRecord> data) {
        this.dataObjArray.addAll(data);
    }

    public int size() {
        return dataObjArray.size();
    }
}
