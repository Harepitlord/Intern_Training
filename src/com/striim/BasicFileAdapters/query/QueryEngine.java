package com.striim.BasicFileAdapters.query;

import com.striim.BasicFileAdapters.database.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class QueryEngine {
    private final Scanner sc;
    private final InMemoryDatabase database;

    private Predicate<DataRecord> query;

    public QueryEngine(Scanner sc, InMemoryDatabase databaseObj) {
        this.database = databaseObj;
        this.sc = sc;
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
    }

    public ArrayList<DataRecord> queryData() {
        if(query==null)
            return database.getDataObjArray();
        return (ArrayList<DataRecord>) database.getDataObjArray().stream().filter(query).collect(Collectors.toList());
    }
}
