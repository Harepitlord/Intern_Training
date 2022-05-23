package com.striim.BasicFileAdapters.writer;

import com.striim.BasicFileAdapters.database.*;
import com.striim.BasicFileAdapters.query.QueryEngine;
import org.json.*;
import java.util.*;

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



    public void writeFile(Scanner sc, InMemoryDatabase database){
        QueryEngine queryEngine=new QueryEngine(sc,database);
        queryEngine.generateQueries();

        ArrayList<DataRecord> toWriteResultSet = queryEngine.queryData();

        JSONArray jsonArray=toJson(toWriteResultSet);
        for(int i=0;i<jsonArray.length();i++){
            try {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                fileWriter.write(jsonObj.toString());
                fileWriter.flush();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
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
