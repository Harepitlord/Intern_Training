package com.striim.BasicFileAdapters.converter;

import com.striim.BasicFileAdapters.query.QueryEngine;
import com.striim.BasicFileAdapters.reader.*;
import com.striim.BasicFileAdapters.writer.*;
import com.striim.BasicFileAdapters.database.*;
import java.util.*;

public class Converter {

    private final ArrayList<Reader> readers;
    private final ArrayList<Writer> writers;
    private final InMemoryDatabase database;
    private final Scanner sc;

    // Constructors
    public Converter(Scanner scanner) {
        this.sc = scanner;
        this.database = new InMemoryDatabase();
        this.readers = new ArrayList<>();
        this.writers = new ArrayList<>();
    }

    // This function adds the received CustomReader into readers if it is unique
    public void addReader(Reader reader) {
        if(!readers.contains(reader)) {
            readers.add(reader);
        }
    }

    // This function adds the received CustomWriter into readers if it is unique
    public void addWriter(Writer writer) {
        if(!writers.contains(writer)) {
            writers.add(writer);
        }
    }

    // The menu gives the list of readers available to the user and creates one as per user choice
    private Reader readerMenu() {
        System.out.println("Available Readers are: (Enter the reader name)");
        for (String s : ReadersWriters.readers.keySet()) {
            System.out.println("-> "+s);
        }
        String choice = sc.nextLine().toUpperCase();
        if(ReadersWriters.readers.containsKey(choice))
            return ReadersWriters.readers.get(choice).getInstance();
        System.out.println("Enter proper option: ");
        return readerMenu();
    }

    // The menu gives the list of writers available to the user and creates one as per user choice
    private Writer writerMenu() {
        System.out.println("Available Writers are: (Enter the writer name)");
        for(String s : ReadersWriters.writers.keySet()) {
            System.out.println("-> "+s);
        }
        String choice = sc.nextLine().toUpperCase();
        if(ReadersWriters.writers.containsKey(choice))
            return ReadersWriters.writers.get(choice).getInstance();
        System.out.println("Enter proper option: ");
        return writerMenu();
    }

    // This function adds the readers to converter interface after its creation as per user choice
    public void addReaders(int n) {
        for(int i = 0;i<n;i++) {
            Reader reader = readerMenu();
            reader.initiate(this.sc);
            this.addReader(reader);
        }
    }

    // This function adds the writers to converter interface after its creation as per user choice
    public void addWriters(int n) {
        for(int i = 0;i<n;i++) {
            Writer writer = writerMenu();
            this.addWriter(writer);
        }
    }


    // This function triggers the file reading and stores the DataObjects, readers created in database object.
    // Then calls the writers to query as user and store it.
    public void convert() {
        // Reading, wrapping data with a class and storing in the database
        readers.forEach(e-> database.addDataObjects(e.readFile()));
        // Writing the data into multiple writers based on the user query
        writers.forEach(e->{    System.out.println("Enter the file path for the "+e.getClass());
                                String filepath=sc.nextLine();
                                QueryEngine queryEngine=new QueryEngine(sc,database);
                                ArrayList<ArrayList<DataRecord>> queriesResultSet=queryEngine.generateQueries();
                                queriesResultSet.forEach(eachQueryResult->e.writeFile(filepath,eachQueryResult));
        });
    }

    // This function closes the file connections of readers and writers by respective cleanUp functions.
    public void cleanUp() {
        readers.forEach(Reader::cleanUp);
    }
}
