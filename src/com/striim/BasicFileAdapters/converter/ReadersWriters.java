package com.striim.BasicFileAdapters.converter;

import com.striim.BasicFileAdapters.reader.*;
import com.striim.BasicFileAdapters.writer.*;

import java.util.HashMap;

public class ReadersWriters {

    public static HashMap<String, Reader> readers = new HashMap<>();
    public static HashMap<String, Writer> writers = new HashMap<>();

    static {
        // Readers
        readers.put("CSV READER",new CsvReader());

        // Writers
        writers.put("JSON WRITER", new JsonWriter());
    }
}
