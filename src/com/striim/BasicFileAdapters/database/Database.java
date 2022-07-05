package com.striim.BasicFileAdapters.database;

import java.util.ArrayList;

public interface Database {

    String getType();
    ArrayList<DataRecord> getDataObjArray(String topicId);

    void addDataObjects(ArrayList<DataRecord> data);
}
