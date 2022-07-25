package com.striim.BasicFileAdapters.converter;

import com.striim.BasicFileAdapters.reader.CsvReader;
import com.striim.BasicFileAdapters.reader.Reader;
import com.striim.BasicFileAdapters.writer.JsonWriter;
import com.striim.BasicFileAdapters.writer.Writer;

import java.util.HashMap;

public class ReadersWritersFactory {

    public static HashMap<String, Reader> readers = new HashMap<>();
    public static HashMap<String, Writer> writers = new HashMap<>();

    static {

        readers.put("CSV READER",new CsvReader());

        writers.put("JSON WRITER", new JsonWriter());
    }

    public static void readerMenu() {
        System.out.println("Available Readers are: (Enter the reader name)");
        for (String s : readers.keySet()) {
            System.out.println("-> "+s);
        }
    }

    public static void writerMenu() {
        System.out.println("Available Writers are: (Enter the writer name)");
        for(String s : writers.keySet()) {
            System.out.println("-> "+s);
        }
    }
}
