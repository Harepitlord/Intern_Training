package com.striim.BasicFileAdapters.writer;

import com.opencsv.CSVWriter;
import com.striim.BasicFileAdapters.database.DataRecord;
import com.striim.BasicFileAdapters.database.StorageSpace;
import com.striim.BasicFileAdapters.query.QueryEngine;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

@XSlf4j(topic = "General")
@Component("CSVWRITER")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CsvWriter extends Writer {

    private CSVWriter csvWriter;

    private char delimiter;

    public CsvWriter() {
        state = "Initiated";
    }

    protected boolean prepareWriter() {
        boolean val = super.prepareWriter();
        if(fileConfig.getDelimiter() != null)
            csvWriter = new CSVWriter(fileWriter,fileConfig.getDelimiter().trim().charAt(0),
                    CSVWriter.NO_QUOTE_CHARACTER,CSVWriter.DEFAULT_ESCAPE_CHARACTER,CSVWriter.DEFAULT_LINE_END);
        return val;
    }

    @Override
    public void writeFile( StorageSpace database, ExecutorService executorService) {
        if(prepareWriter()) {
            errorHandling();
            writeFile(database,executorService);
        }

        QueryEngine queryEngine = new QueryEngine(this,database);
        queryEngine.fetchColumns();

        executorService.submit(()-> {
            long start = System.nanoTime();
            String msg = "Files are being written.";
            userInterface.print(msg);
            log.info(msg);
            ArrayList<DataRecord> toWriteResultSet = queryEngine.queryData();

            log.info("{} -- {} -- \nQueried Data : {}",fileConfig.getType(),fileConfig.getFilePath(),toWriteResultSet.size());

            ArrayList<String> headers = toWriteResultSet.get(0).getHeader();

            csvWriter.writeNext(headers.toArray(new String[0]));

            toWriteResultSet.forEach(e-> csvWriter.writeNext(e.getData().toArray(new String[0])));

            try {
                fileWriter.flush();
                state = "Completed";
                fileWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            msg = "File writing completed : time taken -> " + (System.nanoTime() - start) / 1000000 + " ms";
            userInterface.print(msg);
            log.info(msg);
        });
    }
}
