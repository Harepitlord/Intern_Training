package com.striim.BasicFileAdapters.query;

import com.striim.BasicFileAdapters.database.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class QueryEngine {
    private final Scanner sc;
    private final Database database;
    private ArrayList<String> fetchColumnsSet;
    private Predicate<DataRecord> query;
    private ArrayList<DataRecord> dataRecords;

    public QueryEngine(Scanner sc, Database databaseObj) {
        this.database = databaseObj;
        this.sc = sc;
    }

    private void gatherData() {
        if(StringUtils.equals(database.getType(),"Kafka")) {
            String topicId = null;
            while (topicId == null || topicId.length() == 0) {
                System.out.println("Enter the topic Id: ");
                topicId = sc.nextLine();
            }
            dataRecords = database.getDataObjArray(topicId);
        } else {
            dataRecords = database.getDataObjArray(null);
        }
    }

    public void generateQueries(){

        System.out.println("Enter the queries end with ',' to add more constraint and ';' to end the query");
        System.out.println("eg : col > val,col < val;");
        boolean another = true;
        do {
            String input = sc.nextLine();
            if(input.length()==0 || input.equals(";"))
                return;
            if(input.endsWith(";")) {
                another = false;
                input = input.substring(0,input.length()-1);
            }
            String[] constraints = input.split(",");
            for(String constraint : constraints) {
                if(query == null)
                    query = FilterFactory.getFilter(constraint);
                else {
                    Predicate<DataRecord> temp = FilterFactory.getFilter(constraint);
                    if(temp != null)
                        query = query.and(temp);
                }
            }
        }while(another);
        fetchColumns();
    }

    public ArrayList<DataRecord> queryData() {
        if(query==null)
            return dataRecords;
        return (ArrayList<DataRecord>) dataRecords.stream().filter(query)
                .map(dataRecord -> { DataRecord recordObj = new DataRecord();
                                    for (String s : fetchColumnsSet) {
                                        recordObj.getRecord().put(s, dataRecord.getRecord().get(s));
                                    }
                                    return recordObj; })
                .collect(Collectors.toList());
    }

    public void fetchColumns(){
        Map<String,String> record=dataRecords.get(0).getRecord();
        Set<String> keySet=record.keySet();
        System.out.println("Enter the names of the columns you want to fetch.Enter End when you want to end");
        System.out.println("All "+keySet);
        fetchColumnsSet = new ArrayList<>();
        String colName="Start";
        while(!colName.equals("End")){
            colName=sc.nextLine();
            fetchColumnsSet.add(colName);
        }
        fetchColumnsSet.remove(fetchColumnsSet.size()-1);
    }

}
