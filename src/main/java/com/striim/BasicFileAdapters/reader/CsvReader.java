package com.striim.BasicFileAdapters.reader;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import com.striim.BasicFileAdapters.converter.FileConfig;
import com.striim.BasicFileAdapters.database.DataRecord;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// This class will be used to read data from the csv file and parse it into dataObjects and returns those to
// converter interface
@XSlf4j(topic = "General")
@Component("CSVREADER")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CsvReader extends Reader {

    private CSVReader reader;
    private char delimiter;

    public CsvReader() {
        delimiter = ',';
    }

    protected void errorHandling() {

        super.errorHandling();
        switch (state) {

            case "Only Headers":
                userInterface.print("The given csv file contains only Headers");
                this.fileReader = null;
                this.headers = null;

                log.warn("{} -- {} -- The file contains only headers", fileConfig.getType(), fileConfig.getFilePath());
                break;


            case "CSV Error":
                userInterface.print("The given is in improper format, provide filepath for new file");
                this.fileReader = null;

                log.warn("{} -- {} -- The data is not in csv format",fileConfig.getType(),fileConfig.getFilePath());
                break;

            default: {
                state = "Improper state";
                userInterface.print("State failure");

                log.error("{} -- {} -- The Reader is corrupted",fileConfig.getType(),fileConfig.getFilePath());
            }
        }
        log.info("{} -- {} -- Getting new File configuration",fileConfig.getType(),fileConfig.getFilePath());

        fileConfig = userInterface.getReaderFileConfig();
    }

    protected boolean prepareHeaders() {
        try {
            CSVParser parser = new CSVParserBuilder().withSeparator(delimiter).build();
            reader = new CSVReaderBuilder(fileReader).withCSVParser(parser).build();

            String[] ar = reader.readNext();

            if (ar == null) {
                state = "Data Not Found";
                return false;
            }
            headers = new ArrayList<>(Arrays.asList(ar));
            return true;

        } catch (IOException e) {
            userInterface.print("Reader failed to read the contents of the file");
            state = "Read Error";
            return false;
        }
        catch (CsvValidationException e) {
            userInterface.print("Error in CSV data structure");
            state = "CSV Error";
            return false;
        }
    }

    public ArrayList<DataRecord> readFile() {
        try {
            if (!this.prepareReader(fileConfig)) {
                errorHandling();
                return readFile();
            }

            delimiter = fileConfig.getDelimiter() != null ? fileConfig.getDelimiter().charAt(0) : ',';

            if (headers == null)
                if (!this.prepareHeaders()) {
                    errorHandling();
                    return readFile();
                }

            List<String[]> arr = reader.readAll();
            if (arr.size() == 0) {
                state = "Only Headers";
                errorHandling();
                return readFile();
            }

            ArrayList<DataRecord> dataRecords = new ArrayList<>();

            arr.forEach(e-> dataRecords.add(new DataRecord(headers,e)));

            state = "Success";
            this.fileReader.close();

            log.info("{} -- {} -- \nThe file has been successfully read and parsed",fileConfig.getType(),fileConfig.getFilePath());
            log.info("{} -- {} -- \nRecords read : {}",fileConfig.getType(),fileConfig.getFilePath(),String.valueOf(dataRecords.size()));
            return dataRecords;

        } catch (IOException e) {
            userInterface.print("Reader failed to read the contents of the file");
            state = "Read Error";
            errorHandling();
            return readFile();

        } catch (CsvException e) {
            userInterface.print("Error in parsing the csv");
            state = "CSV Error";
            errorHandling();
            return readFile();
        }
    }

    @Override
    public void setFileConfig(FileConfig fileConfig) {
        super.setFileConfig(fileConfig);
        delimiter = fileConfig.getDelimiter().charAt(0);
    }

    protected void finalize() {
        try {
            this.fileReader.close();
            this.reader.close();
        } catch (IOException e) {
            userInterface.print("Error in closing the file");
        }
    }

}
