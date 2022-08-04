package com.striim.BasicFileAdapters.reader;

import com.striim.BasicFileAdapters.converter.FileConfig;
import com.striim.BasicFileAdapters.database.DataRecord;
import lombok.extern.slf4j.XSlf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@XSlf4j(topic = "General")
@Component("JSONREADER")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JsonReader extends Reader {

    private JSONArray jsonArray;

    protected void errorHandling() {
        super.errorHandling();
        fileConfig = userInterface.getReaderFileConfig();
    }

    private String[] jsonToString(HashMap<String,String> jsonObject ) {
        ArrayList<String> objects = (ArrayList<String>) (jsonObject.keySet().stream().map(jsonObject::get).collect(Collectors.toList()));
        try {
            return objects.toArray(new String[0]);

        }catch (ClassCastException e) {
            log.error("Improper Data in json");
            return null;
        }
    }

    @Override
    protected boolean prepareReader(FileConfig fileConfig) {
        if(super.prepareReader(fileConfig))
            return true;

        JSONTokener tokener = new JSONTokener(fileReader);
        JSONObject jsonObject = new JSONObject(tokener);
        String key = jsonObject.keySet().stream()
                .filter(e->e.contains("Data")||e.contains("data")).collect(Collectors.toList()).get(0);

        jsonArray = jsonObject.getJSONArray(key);

        return true;
    }

    @Override
    protected boolean prepareHeaders() {
        try {

            JSONObject header = (JSONObject) jsonArray.get(0);

            if(header.keySet().size() < 1){
                state = "Data Not Found";
                return false;
            }
            headers = new ArrayList<>(header.keySet());
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public ArrayList<DataRecord> readFile() {
        try {
            if (this.prepareReader(fileConfig)) {
                errorHandling();
                return readFile();
            }

            if(headers == null)
                if (!this.prepareHeaders()) {
                    errorHandling();
                    return readFile();
            }

            List<HashMap<String,String>> jsonObjects = jsonArray.toList().stream().map(e->(HashMap<String,String>)e)
                    .filter(e->e.size() == headers.size()).collect(Collectors.toList());

            ArrayList<DataRecord> dataRecords = new ArrayList<>();


            jsonObjects.forEach(jsonObject -> {
                String[] strings = jsonToString(jsonObject);
                if (strings != null)
                    dataRecords.add(new DataRecord(headers, strings));
                });

            state = "Success";


            log.info("{} -- {} -- \nThe file has been successfully read and parsed",fileConfig.getType(),fileConfig.getFilePath());
            log.info("{} -- {} -- \nRecords read : {}",fileConfig.getType(),fileConfig.getFilePath(),String.valueOf(dataRecords.size()));
            return dataRecords;

        } catch (Exception e) {
            userInterface.print("Reader failed to read the contents of the file");
            state = "Read Error";
            errorHandling();
            return readFile();
        }
    }

    @Override
    protected void finalize() {
        try {
            this.fileReader.close();
        } catch (IOException e) {
            userInterface.print("Error in closing the file");
        }
    }
}
