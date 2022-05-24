package com.striim.BasicFileAdapters.writer;

import com.striim.BasicFileAdapters.database.*;
import com.striim.BasicFileAdapters.query.QueryEngine;
import org.json.*;
import java.util.*;
import java.util.concurrent.ExecutorService;

public class JsonWriter extends Writer {

    public JsonWriter() {
        state = "Initiated";
    }

    public Writer getInstance(String filePath) {
        Writer writer =  new JsonWriter();
        if(writer.prepareWriter(filePath))
            return writer;
        else
            return null;
    }



    public void writeFile(Scanner sc, InMemoryDatabase database, ExecutorService executorService){
        QueryEngine queryEngine=new QueryEngine(sc,database);
        queryEngine.generateQueries();

        executorService.submit(() -> {
            ArrayList<DataRecord> toWriteResultSet = queryEngine.queryData();

            JSONArray jsonArray=toJson(toWriteResultSet);
            JSONObject jsonObj=new JSONObject();
            try {
                jsonObj.put("JSON Data",jsonArray);
                fileWriter.write(jsonObj.toString());
                fileWriter.flush();
                state = "Completed";
            }catch(Exception ex){
                ex.printStackTrace();
            }
        });


    }

    JSONArray toJson(ArrayList<DataRecord> toWriteResultSet){
        JSONArray arrayObj=new JSONArray();
        for (DataRecord dataRecord : toWriteResultSet) {
            JSONObject jsonObj = new JSONObject(dataRecord.getRecord());
            arrayObj.put(jsonObj);
        }
        return arrayObj;
    }

    @Override
    public String getName() {
        return "JSON WRITER";
    }
}
