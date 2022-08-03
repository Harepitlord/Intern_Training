package com.striim.BasicFileAdapters.writer;

import com.striim.BasicFileAdapters.database.DataRecord;
import com.striim.BasicFileAdapters.database.StorageSpace;
import com.striim.BasicFileAdapters.query.QueryEngine;
import lombok.extern.slf4j.XSlf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

@XSlf4j(topic = "General")
@Component("JSONWRITER")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JsonWriter extends Writer {

    public JsonWriter() {
        state = "Initiated";
    }


    public void writeFile(Writer writer, StorageSpace database, ExecutorService executorService) {
        if (!prepareWriter()) {
            errorHandling();
            writeFile(writer, database, executorService);
        }
        this.userInterface = writer.userInterface;
        QueryEngine queryEngine = new QueryEngine(writer, database);
        queryEngine.fetchColumns();

        executorService.submit(() -> {
            long start = System.nanoTime();
            String msg = "Files are being written.";
            userInterface.print(msg);
            log.info(msg);
            ArrayList<DataRecord> toWriteResultSet = queryEngine.queryData();

            log.info("{} -- {} -- \nQueried Data : {}",fileConfig.getType(),fileConfig.getFilePath(),toWriteResultSet.size());

            JSONArray jsonArray = toJson(toWriteResultSet);
            JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put("JSON Data", jsonArray);
                fileWriter.write(jsonObj.toString());
                fileWriter.flush();
                state = "Completed";
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            msg = "File writing completed : time taken -> " + (System.nanoTime() - start) / 1000000 + " ms";
            userInterface.print(msg);
            log.info(msg);
        });
    }

    JSONArray toJson(ArrayList<DataRecord> toWriteResultSet){
        JSONArray arrayObj=new JSONArray();
        for (DataRecord dataRecord : toWriteResultSet) {
            JSONObject jsonObj = new JSONObject(dataRecord.getRecords());
            arrayObj.put(jsonObj);
        }
        return arrayObj;
    }

}
