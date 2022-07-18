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

        readers.put("CSV READER",new CsvReader(null));

        writers.put("JSON WRITER", new JsonWriter());
    }

    public static void readerMenu() {
        System.out.println("Available Readers are: (Enter the reader name)");
        for (String s : readers.keySet()) {
            System.out.println("-> "+s);
        }
    }

    public static Reader getReader(String[] input) {
        String choice = input[0].toUpperCase(),filePath = input[1];
        if(readers.containsKey(choice))
            return readers.get(choice).getInstance(filePath);
        return null;
    }

    public static void writerMenu() {
        System.out.println("Available Writers are: (Enter the writer name)");
        for(String s : writers.keySet()) {
            System.out.println("-> "+s);
        }
    }

    public static Writer getWriter(String[] input) {
        String choice = input[0].toUpperCase(),filePath = input[1],fileName = input[2];
        if (writers.containsKey(choice))
            return writers.get(choice).getInstance(filePath+"/"+fileName);
        return null;
    }
}
