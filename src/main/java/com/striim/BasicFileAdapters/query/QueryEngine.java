package com.striim.BasicFileAdapters.query;

import com.striim.BasicFileAdapters.UserInterface.UserInterface;
import com.striim.BasicFileAdapters.database.DataRecord;
import com.striim.BasicFileAdapters.database.StorageSpace;
import com.striim.BasicFileAdapters.writer.Writer;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j(topic = "General")
public class QueryEngine {
    private final UserInterface userInterface;
    private final StorageSpace database;
    private final Writer writer;
    private Predicate<DataRecord> query;
    private ArrayList<String> fetchColumnsSet;

    public QueryEngine(Writer writer, StorageSpace databaseObj) {
        this.database = databaseObj;
        this.userInterface = writer.getUserInterface();
        this.writer = writer;
    }

    public ArrayList<DataRecord> queryData() {
        if (query == null || fetchColumnsSet.size()==0) {
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
        userInterface.generateQueries(writer.getFileConfig(), keySet);
        query = writer.getQuery();
        userInterface.fetchColumns(writer.getFileConfig(), keySet);
        fetchColumnsSet = writer.getFetchColumns();

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