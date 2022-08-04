package com.striim.BasicFileAdapters.database;

import java.util.ArrayList;
import java.util.HashMap;

// This class acts as a wrapper class to wrap a record of data with respective headers.
public class DataRecord {

    HashMap<String, String> records;

    public DataRecord() {
        records = new HashMap<>();
    }

    public DataRecord(ArrayList<String> headers, String[] dat) {
        this.records = new HashMap<>();
        for (int i = 0; i < dat.length; i++)
            this.records.put(headers.get(i), dat[i]);
    }

    public HashMap<String, String> getRecords() {
        return records;
    }

    public String get(String key) {
        return records.getOrDefault(key, null);
    }

    public void put(String key, String value) {
        records.put(key, value);
    }

    public DataRecord fetchColumn(ArrayList<String> fetchColumns) {
        DataRecord recordObj = new DataRecord();
        for (String s : fetchColumns) {
            recordObj.put(s, get(s));
        }
        return recordObj;
    }

    public ArrayList<String> getHeader() {
        return new ArrayList<>(records.keySet());
    }

    public ArrayList<String> getData() {
        return new ArrayList<>(records.values());
    }
}
