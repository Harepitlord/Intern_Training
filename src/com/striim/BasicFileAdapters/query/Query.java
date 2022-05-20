package com.striim.BasicFileAdapters.query;

import java.util.*;
import com.striim.BasicFileAdapters.database.*;
import com.striim.BasicFileAdapters.filters.*;

public class Query {

    private final Scanner sc;
    private final ArrayList<Filter> filterArray;
    private final InMemoryDatabase dataBaseObj;
    private final ArrayList<DataRecord> resultSet;

    Query(Scanner sc, InMemoryDatabase dataBaseObj)
    {
        this.dataBaseObj=dataBaseObj;
        this.sc=sc;
        resultSet=new ArrayList<>();
        filterArray=new ArrayList<>();
    }

    public void addFilters(){
        System.out.println("Enter the number of filters you want to add");
        int numOfFilters=sc.nextInt();
        sc.nextLine();
        for(int i=0;i<numOfFilters;i++)
        {
            System.out.println("Enter the Column Name on which the Constraint has to be applied");
            String columnName=sc.nextLine();
            System.out.println("Enter the Compare with value to which the values has to be compared");
            String compareWithValue=sc.nextLine();
            filterArray.add(createFilter(columnName,compareWithValue));
        }
    }

    public Filter createFilter(String columnName,String compareWithValue){
        System.out.println("Enter the type of filter from the Menu");
        HashMap<String,Filter> filterType=new HashMap<>();
        filterType.put("Greater Than",new GreaterThanFilter());
        filterType.put("Lesser Than",new LesserThanFilter());
        filterType.put("Equal To",new EqualToFilter());
        System.out.println(filterType.keySet());
        return (filterType.get(sc.nextLine())).getInstance(columnName,compareWithValue);
    }

    public ArrayList<DataRecord> getSatisfiedRecords(){
        for(int i = 0; i< dataBaseObj.getDataObjArray().size(); i++){
            boolean recordSatisfies=isAllConditionsSatisfied(dataBaseObj.getDataObjArray().get(i));
            if(recordSatisfies){
                resultSet.add(dataBaseObj.getDataObjArray().get(i));
            }
        }
        return resultSet;
    }

    public ArrayList<DataRecord> fetchColumns(ArrayList<DataRecord> resultSet){
        Map<String,String> record=resultSet.get(0).getRecord();
        Set<String> keySet=record.keySet();
        System.out.println("Enter the names of the columns you want to fetch.Enter End when you want to end");
        System.out.println("All "+keySet);
        ArrayList<String> fetchColumnsSet = new ArrayList<>();
        String colName="Start";
        while(!colName.equals("End")){
            colName=sc.nextLine();
            fetchColumnsSet.add(colName);
        }
        fetchColumnsSet.remove(fetchColumnsSet.size()-1);
        if(fetchColumnsSet.get(0).equals("All")){
            return resultSet;
        }
        else{
            //need to add only the required columns to a arraylist of dataobjects which gives only the specific columns
            ArrayList<DataRecord> toWriteResultSet=new ArrayList<>();
            for (DataRecord dataRecord : resultSet) {
                DataRecord recordObj = new DataRecord();
                for (String s : fetchColumnsSet) {
                    recordObj.getRecord().put(s, dataRecord.getRecord().get(s));
                }
                toWriteResultSet.add(recordObj);
            }
            return toWriteResultSet;
        }
    }

    private boolean isAllConditionsSatisfied(DataRecord dataObj){
        int i=0;
        boolean allSatisfied;
        do{
            allSatisfied=filterArray.get(i).doesSatisfies(dataObj);
            i++;
        }while(allSatisfied && i<filterArray.size());
        return allSatisfied;
    }

}
