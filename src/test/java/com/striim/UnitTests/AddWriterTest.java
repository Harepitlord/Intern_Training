package com.striim.UnitTests;

import com.striim.BasicFileAdapters.converter.Converter;
import com.striim.BasicFileAdapters.converter.FileConfig;
import com.striim.BasicFileAdapters.writer.JsonWriter;
import com.striim.BasicFileAdapters.writer.Writer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AddWriterTest {

    static Converter converter;

    @BeforeAll
    static void writersSetup(){
        converter=new Converter();
        Writer writer1=new JsonWriter();
        FileConfig file1=new FileConfig();
        file1.setFilePath("/Users/aathithyamj/IdeaProjects/CsvToJson/src/main/java/com/striim/SampleData/bank1.json");
        writer1.setFileConfig(file1);
        converter.addWriter(writer1);
    }

    @ParameterizedTest
    @CsvSource({"/Users/aathithyamj/IdeaProjects/CsvToJson/src/main/java/com/striim/SampleData/bank.json,true","/Users/aathithyamj/IdeaProjects/CsvToJson/src/main/java/com/striim/SampleData/bank1.json,false"})
    void validateAddWriter(String filepath,String expected) {
        int initialSize=converter.writers.size();
        Writer writer2=new JsonWriter();
        FileConfig fileObj=new FileConfig();
        fileObj.setFilePath(filepath);
        writer2.setFileConfig(fileObj);
        converter.addWriter(writer2);
        Assertions.assertEquals(Boolean.parseBoolean(expected),converter.writers.size()==initialSize+1);
    }
}