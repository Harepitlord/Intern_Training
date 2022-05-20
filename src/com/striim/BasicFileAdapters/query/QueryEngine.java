package com.striim.BasicFileAdapters.query;

import com.striim.BasicFileAdapters.database.*;
import java.util.*;

public class QueryEngine {
    Scanner sc;
    InMemoryDatabase dataBaseObj;

    public QueryEngine(Scanner sc, InMemoryDatabase databaseObj) {
        this.dataBaseObj = databaseObj;
        this.sc = sc;
    }

    public ArrayList<ArrayList<DataRecord>> generateQueries(){
        ArrayList<ArrayList<DataRecord>> queriesResultSet=new ArrayList<>();
        System.out.println("Enter the number of queries you want to enter");
        int n=sc.nextInt();
        for(int i=0;i<n;i++){
            Query query=new Query(sc,dataBaseObj);
            query.addFilters();
            ArrayList<DataRecord> result=query.getSatisfiedRecords();
            ArrayList<DataRecord> finalResult=query.fetchColumns(result);
            queriesResultSet.add(finalResult);
        }
        return queriesResultSet;
    }
}
