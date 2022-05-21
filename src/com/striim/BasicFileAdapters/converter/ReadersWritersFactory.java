package com.striim.BasicFileAdapters.converter;

import com.striim.BasicFileAdapters.reader.*;
import com.striim.BasicFileAdapters.writer.*;

import java.util.HashMap;
import java.util.Scanner;

public class ReadersWritersFactory {

    public static HashMap<String, Reader> readers = new HashMap<>();
    public static HashMap<String, Writer> writers = new HashMap<>();

    static {

        readers.put("CSV READER",new CsvReader());

        writers.put("JSON WRITER", new JsonWriter());
    }

    public static Reader readerMenu(Scanner scanner) {
        System.out.println("Available Readers are: (Enter the reader name)");
        for (String s : ReadersWritersFactory.readers.keySet()) {
            System.out.println("-> "+s);
        }
        String choice = scanner.nextLine().toUpperCase();
        if(ReadersWritersFactory.readers.containsKey(choice))
            return ReadersWritersFactory.readers.get(choice).getInstance();
        System.out.println("Enter proper option: ");
        return readerMenu(scanner);
    }

    public static Writer writerMenu(Scanner scanner) {
        System.out.println("Available Writers are: (Enter the writer name)");
        for(String s : ReadersWritersFactory.writers.keySet()) {
            System.out.println("-> "+s);
        }
        String choice = scanner.nextLine().toUpperCase();
        if(ReadersWritersFactory.writers.containsKey(choice))
            return ReadersWritersFactory.writers.get(choice).getInstance();
        System.out.println("Enter proper option: ");
        return writerMenu(scanner);
    }
}
