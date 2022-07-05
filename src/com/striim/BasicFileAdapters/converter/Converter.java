package com.striim.BasicFileAdapters.converter;

import com.striim.BasicFileAdapters.database.Database;
import com.striim.BasicFileAdapters.database.InMemoryDatabase;
import com.striim.BasicFileAdapters.reader.Reader;
import com.striim.BasicFileAdapters.writer.Writer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class Converter {

    private final ArrayList<Reader> readers;
    private final ArrayList<Writer> writers;
    private final Database database;
    private final Scanner sc;

    public Converter(Scanner scanner,Database database) {
        this.sc = scanner;
        this.database = database;
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
                if(readers.size()>0)
                    return;
                else
                    continue;
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
            reader.initiate(this.sc,database.getType());
            this.addReader(reader);
        }while(another);
    }

    public void writers(ExecutorService executorService) {
        ReadersWritersFactory.writerMenu();
        boolean another = true;
        do {
            System.out.println("Enter the writer type, folder path and file name separated by a comma : (End with ; to end the input)");
            String input = sc.nextLine();
            if(input.length() == 0 || input.equals(";"))
                if(writers.size()>0)
                    return;
                else
                    continue;
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
                writer.writeFile(sc,database,executorService);
            }
        }while (another);
    }

    public void readFiles(ExecutorService executorService) {
        List<Future<?>> futures = readers.stream().map(e-> executorService.submit(() -> database.addDataObjects(e.readFile()))).collect(Collectors.toList());
        boolean loop = true;
        long start = System.nanoTime();
        System.out.print("Files are being read .");
        while(loop) {
            loop = !futures.stream().allMatch(Future::isDone);
        }
        System.out.println("File reading completed : time taken -> "+(System.nanoTime()-start));
    }

}
