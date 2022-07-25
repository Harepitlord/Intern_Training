package com.striim.BasicFileAdapters.reader;

import com.striim.BasicFileAdapters.database.*;
import com.opencsv.*;
import com.opencsv.exceptions.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

// This class will be used to read data from the csv file and parse it into dataObjects and returns those to
// converter interface
@Component("CSVREADER")
public class CsvReader extends Reader {

    private CSVReader reader;
    private char delimiter;

    public CsvReader() {
        delimiter = ',';
    }

    private ArrayList<DataRecord> errorHandling() {
        switch (state) {
            case "File Not Found":
                System.out.println("There is no such file in the given path, Re-enter new File Path");
                break;

            case "Data Not Found":
                System.out.println("The given csv file doesn't contain a record");
                this.fileReader = null;
                break;

            case "Only Headers":
                System.out.println("The given csv file contains only Headers");
                this.fileReader = null;
                this.headers = null;
                break;

            case "Read Error":
                System.out.println("The given file not readable, provide filepath for the new file");
                this.fileReader = null;
                break;

            case "CSV Error":
                System.out.println("The given is in improper format, provide filepath for new file");
                this.fileReader = null;
                break;

            default: {
                state = "Improper state";
                System.out.println("State failure");
                return null;
            }
        }
        fileConfig = userInterface.getReaderFileConfig();
        return readFile();
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
            System.out.println("Reader failed to read the contents of the file");
            state = "Read Error";
            return false;
        }
        catch (CsvValidationException e) {
            System.out.println("Error in CSV data structure");
            state = "CSV Error";
            return false;
        }
    }

    public ArrayList<DataRecord> readFile() {
        try {
            if (!this.prepareReader(fileConfig))
                return errorHandling();

            delimiter = fileConfig.getDelimiter().charAt(0);

            if(headers == null)
                if (!this.prepareHeaders())
                    return errorHandling();

            List<String[]> arr = reader.readAll();
            if(arr.size()==0) {
                state = "Only Headers";
                return errorHandling();
            }

            ArrayList<DataRecord> dataRecords = new ArrayList<>();

            arr.forEach(e-> dataRecords.add(new DataRecord(headers,e)));

            state = "Success";
            this.fileReader.close();
            return dataRecords;

        } catch (IOException e) {
            System.out.println("Reader failed to read the contents of the file");
            state = "Read Error";
            return null;
        } catch (CsvException e) {
            System.out.println("Error in parsing the csv");
            state = "CSV Error";
            return null;
        }
    }

    protected void finalize() {
        try {
            this.fileReader.close();
            this.reader.close();
        } catch (IOException e) {
            System.out.println("Error in closing the file");
        }
    }

}
