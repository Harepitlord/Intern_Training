package com.striim.BasicFileAdapters.query;

import com.striim.BasicFileAdapters.UserInterface.UserInterface;
import com.striim.BasicFileAdapters.database.DataRecord;
import com.striim.BasicFileAdapters.database.StorageSpace;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j(topic = "General")
public class QueryEngine {
    private final UserInterface userInterface;
    private final StorageSpace database;
    private ArrayList<String> fetchColumnsSet;

    private Predicate<DataRecord> query;

    public QueryEngine(UserInterface userInterface, StorageSpace databaseObj) {
        this.database = databaseObj;
        this.userInterface = userInterface;
    }

    public ArrayList<DataRecord> queryData() {
        if (query == null || fetchColumnsSet.get(0).equals("All")) {
            log.info("Query is empty or All columns options selected");
            return database.getDataObjArray();
        }
        return (ArrayList<DataRecord>) database.getDataObjArray().stream().filter(query)
                .map(dataRecord -> dataRecord.fetchColumn(fetchColumnsSet))
                .collect(Collectors.toList());
    }

    public void fetchColumns() {
        Map<String, String> record = database.getDataObjArray().get(0).getRecords();
        ArrayList<String> keySet = new ArrayList<>(record.keySet());
        query = userInterface.generateQueries(keySet);
        fetchColumnsSet = userInterface.fetchColumns(keySet);
        if (fetchColumnsSet.size() == 1) {
            fetchColumnsSet.remove(0);
            fetchColumnsSet.addAll(keySet);
        } else {
            fetchColumnsSet.remove(fetchColumnsSet.size() - 1);
        }
    }

    public static boolean isProperConstraint(String constraint,ArrayList<String> keySet){
        String[] temp = constraint.split(" ");
        return (temp.length==3) && isValidColumn(temp[0],keySet) && isValidOperation(temp[1]);
    }

    private static boolean isValidColumn(String colName,ArrayList<String> keySet){
        return keySet.contains(colName);
    }

    private static boolean isValidOperation(String op){
        return FilterFactory.getAvailableOperation().contains(op);
    }

}