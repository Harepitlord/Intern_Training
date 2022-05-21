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

    public Converter(Scanner scanner) {
        this.sc = scanner;
        this.database = new InMemoryDatabase();
        this.readers = new ArrayList<>();
        this.writers = new ArrayList<>();
    }

    public void addReader(Reader reader) {
        if(!readers.contains(reader)) {
            readers.add(reader);
        }
    }

    public void addWriter(Writer writer) {
        if(!writers.contains(writer)) {
            writers.add(writer);
        }
    }

    public void addReaders(int n) {
        for(int i = 0;i<n;i++) {
            Reader reader = ReadersWritersFactory.readerMenu(sc);
            reader.initiate(this.sc);
            this.addReader(reader);
        }
    }

    public void addWriters(int n) {
        for(int i = 0;i<n;i++) {
            Writer writer = ReadersWritersFactory.writerMenu(sc);
            this.addWriter(writer);
        }
    }

    private void writerOperation(Writer writer) {
        System.out.println("Enter the file path for the "+writer.getName());
        String filepath=sc.nextLine();
        QueryEngine queryEngine=new QueryEngine(sc,database);
        ArrayList<ArrayList<DataRecord>> queriesResultSet=queryEngine.generateQueries();
        queriesResultSet.forEach(eachQueryResult->writer.writeFile(filepath,eachQueryResult));
    }

    public void convert() {

        readers.forEach(e-> database.addDataObjects(e.readFile()));

        writers.forEach(this::writerOperation);
    }

    public void cleanUp() {
        readers.forEach(Reader::cleanUp);
    }
}
