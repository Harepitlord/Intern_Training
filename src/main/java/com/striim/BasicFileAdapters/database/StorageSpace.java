package com.striim.BasicFileAdapters.database;

import java.util.ArrayList;

public interface StorageSpace {
    void addDataObjects(ArrayList<DataRecord> data);

    ArrayList<DataRecord> getDataObjArray();

    String getClassName();
}
