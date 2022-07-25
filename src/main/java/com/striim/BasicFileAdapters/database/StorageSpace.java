package com.striim.BasicFileAdapters.database;

import java.util.ArrayList;

public interface StorageSpace {
    public void addDataObjects(ArrayList<DataRecord> data);
    public ArrayList<DataRecord> getDataObjArray();
}
