package com.striim.BasicFileAdapters.writer;

import com.striim.BasicFileAdapters.database.*;
import org.json.*;
import java.util.*;

public class JsonWriter extends Writer {

    public Writer getInstance() {
        return new JsonWriter();
    }

    public void writeFile(String filepath, ArrayList<DataRecord> toWriteResultSet){
        prepareWriter(filepath);
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
