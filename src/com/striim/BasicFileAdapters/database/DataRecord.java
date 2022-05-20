package com.striim.BasicFileAdapters.database;

import java.util.ArrayList;
import java.util.HashMap;

// This class acts as an wrapper class to wrap a record of data with respective headers.
public class DataRecord {

    HashMap<String,String> record;

    public DataRecord() {
        record = new HashMap<>();
    }

    public DataRecord(ArrayList<String> headers, String[] dat) {
        this.record = new HashMap<>();
        for(int i = 0;i<dat.length;i++)
            this.record.put(headers.get(i),dat[i]);
    }

    public HashMap<String,String> getRecord(){
        return record;
    }

    public void setRecord(HashMap<String, String> record) {
        this.record = record;
    }

    public String get(String key) {
        return record.getOrDefault(key,null);
    }

    public void put(String key,String value) {
        record.put(key, value);
    }
}
