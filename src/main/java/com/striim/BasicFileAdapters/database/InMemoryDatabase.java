package com.striim.BasicFileAdapters.database;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

// This class acts as Java-based imitation of database
@Component("INMEMORYDATABASE")
public class InMemoryDatabase extends StorageSpace {

    private final ArrayList<DataRecord> dataObjArray = new ArrayList<>();

    @Override
    public ArrayList<DataRecord> getDataObjArray() {
        return dataObjArray;
    }

    @Override
    public void addDataObjects(ArrayList<DataRecord> data) {
        this.dataObjArray.addAll(data);
    }

    public int size() {
        return dataObjArray.size();
    }

    @Override
    public String getClassName() {
        return "InMemoryDatabase";
    }
}
