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

    public boolean addWriter(Writer writer) {
        if(!writers.contains(writer)) {
            writers.add(writer);
            return true;
        }
        return false;
    }

    public void addReaders() {
        ReadersWritersFactory.readerMenu();
        boolean another = true;
        do {
            System.out.println("Enter the reader type and file path separated by a comma: (End with ; to end the input)");
            String input = sc.nextLine();
            if(input.length() == 0 || input.equals(";"))
                return;
            if(input.endsWith(";")) {
                another = false;
                input = input.substring(0, input.length() - 1);
            }
            Reader reader = ReadersWritersFactory.getReader(input.split(","));
            if(reader == null) {
                another = true;
                System.out.println("Enter proper reader type and file path");
                continue;
            }
            reader.initiate(this.sc);
            this.addReader(reader);
        }while(another);
    }

    public void writers() {
        ReadersWritersFactory.writerMenu();
        boolean another = true;
        do {
            System.out.println("Enter the writer type, folder path and file name separated by a comma : (End with ; to end the input)");
            String input = sc.nextLine();
            if(input.length() == 0 || input.equals(";"))
                return;
            if (input.endsWith(";")) {
                another = false;
                input = input.substring(0, input.length() - 1);
            }
            Writer writer = ReadersWritersFactory.getWriter(input.split(","));
            if(writer == null) {
                another = true;
                System.out.println("Enter proper writer type and file path");
                continue;
            }
            if(this.addWriter(writer)) {
                writer.writeFile(sc,database);
            }
        }while (another);
    }



    public void readFiles() {
        readers.forEach(e-> database.addDataObjects(e.readFile()));
    }

}
