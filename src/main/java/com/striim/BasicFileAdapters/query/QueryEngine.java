//package com.striim.BasicFileAdapters.query;
//
//import com.striim.BasicFileAdapters.database.*;
//
//import java.util.*;
//import java.util.function.Predicate;
//import java.util.stream.Collectors;
//
//public class QueryEngine {
//    private final Scanner sc;
//    private final InMemoryDatabase database;
//    private ArrayList<String> fetchColumnsSet;
//
//    private Predicate<DataRecord> query;
//
//    public QueryEngine(Scanner sc, InMemoryDatabase databaseObj) {
//        this.database = databaseObj;
//        this.sc = sc;
//    }
//
//    public void generateQueries(){
//
//        System.out.println("Enter the queries end with ',' to add more constraint and ';' to end the query");
//        System.out.println("eg : col > val,col < val;");
//        boolean another = true;
//        do {
//            String input = sc.nextLine();
//            if(input.length()==0 || input.equals(";"))
//                return;
//            if(input.endsWith(";")) {
//                another = false;
//                input = input.substring(0,input.length()-1);
//            }
//            String[] constraints = input.split(",");
//            for(String constraint : constraints) {
//                if(query == null)
//                    query = FilterFactory.getFilter(constraint);
//                else {
//                    Predicate<DataRecord> temp = FilterFactory.getFilter(constraint);
//                    if(temp != null)
//                        query = query.and(temp);
//                }
//            }
//        }while(another);
//        fetchColumns();
//    }
//
//    public ArrayList<DataRecord> queryData() {
//        if(query==null || fetchColumnsSet.get(0).equals("All") )
//            return database.getDataObjArray();
//        return (ArrayList<DataRecord>) database.getDataObjArray().stream().filter(query)
//                .map(dataRecord -> dataRecord.fetchColumn(fetchColumnsSet))
//                .collect(Collectors.toList());
//    }
//
//    public void fetchColumns(){
//        Map<String,String> record=database.getDataObjArray().get(0).getRecord();
//        Set<String> keySet=record.keySet();
//        System.out.println("Enter the names of the columns you want to fetch.Enter End when you want to end.For all columns enter end");
//        System.out.println("All "+keySet);
//        fetchColumnsSet = new ArrayList<>();
//        String colName="Start";
//        while(!colName.equals("END")){
//            colName=sc.nextLine().toUpperCase();
//            fetchColumnsSet.add(colName);
//        }
//        if(fetchColumnsSet.size()<=1)
//            fetchColumnsSet.addAll(keySet);
//        fetchColumnsSet.remove(fetchColumnsSet.size()-1);
//    }
//
//}
package com.striim.BasicFileAdapters.query;

import com.striim.BasicFileAdapters.UserInterface.UserInterface;
import com.striim.BasicFileAdapters.database.DataRecord;
import com.striim.BasicFileAdapters.database.StorageSpace;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        if (query == null || fetchColumnsSet.get(0).equals("All"))
            return database.getDataObjArray();
        return (ArrayList<DataRecord>) database.getDataObjArray().stream().filter(query)
                .map(dataRecord -> dataRecord.fetchColumn(fetchColumnsSet))
                .collect(Collectors.toList());
    }

    public void fetchColumns() {
        Map<String, String> record = database.getDataObjArray().get(0).getRecord();
        ArrayList<String> keySet = new ArrayList<>(record.keySet());
        query = userInterface.generateQueries(keySet);
        fetchColumnsSet = userInterface.fetchColumns(keySet);
        if (fetchColumnsSet.size()==1) {
            fetchColumnsSet.remove(0);
            fetchColumnsSet.addAll(keySet);
        }
        else {
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