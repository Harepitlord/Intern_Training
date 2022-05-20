package com.striim.BasicFileAdapters.reader;

import com.striim.BasicFileAdapters.database.*;
import com.opencsv.*;
import com.opencsv.exceptions.*;
import java.io.*;
import java.util.*;

// This class will be used to read data from the csv file and parse it into dataObjects and returns those to
// converter interface
public class CsvReader extends Reader {

    private CSVReader reader;
    private char delimiter;

    public CsvReader() {
        super();
        delimiter = ',';
    }

    // This function is used to create new instance of this class for each CSV-based readers
    public Reader getInstance() {
        return new CsvReader();
    }

    // This function gets the file path from the user and sets it to the data member
    private void getFilePath() {
        System.out.println("Enter the File path for the csv file: ");
        String temp = scanner.nextLine();
        if(temp.length()>0)
            this.filePath = temp;
    }

    // This function handles if the reader process encounters any expected exceptions
    private ArrayList<DataRecord> errorHandling() {
        switch (state) {
            case "File Not Found": {
                System.out.println("There is no such file in the given path, Re-enter new File Path");
                return readFile();
            }
            case "Data Not Found": {
                System.out.println("The given csv file doesn't contain a record");
                this.fileReader = null;
                return readFile();
            }
            case "Only Headers": {
                System.out.println("The given csv file contains only Headers");
                this.fileReader = null;
                this.headers = null;
                return readFile();
            }
            case "Read Error": {
                System.out.println("The given file not readable, provide filepath for the new file");
                this.fileReader = null;
                return readFile();
            }
            case "CSV Error": {
                System.out.println("The given is in improper format, provide filepath for new file");
                this.fileReader = null;
                return readFile();
            }
            default: {
                state = "Improper state";
                System.out.println("State failure");
                return null;
            }

        }
    }

    // This function initiates the data members with values from the user
    @Override
    public void initiate(Scanner sc) {
        scanner = sc;
        System.out.print("Enter the delimiter for the data in csv (Leave blank for ','): ");
        String temp = scanner.nextLine();
        if(temp.length()>1) {
            delimiter = temp.charAt(0);
        }
    }

    // This function creates the CSV parser and gets the header from the file ( assuming headers are present as first record)
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

    // This function reads the file and calls the parser to parse the data and wraps the record into DataObject and
    // returns a collection of DataObjects to converter interface
    public ArrayList<DataRecord> readFile() {
        try {
            if(fileReader == null) {
                getFilePath();
                if (!this.prepareReader(filePath))
                    return errorHandling();
            }

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

    // This function closes the file connections of the reader
    public void cleanUp() {
        try {
            this.fileReader.close();
            this.reader.close();
        } catch (IOException e) {
            System.out.println("Error in closing the file");
        }
    }

}
