package com.striim.UnitTests;

import com.striim.BasicFileAdapters.converter.Converter;
import com.striim.BasicFileAdapters.converter.FileConfig;
import com.striim.BasicFileAdapters.reader.CsvReader;
import com.striim.BasicFileAdapters.reader.Reader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AddReaderTest {

    static Converter converter;

    @BeforeAll
    static void readersSetup(){
        converter=new Converter();
        Reader reader1=new CsvReader();
        FileConfig file1=new FileConfig();
        file1.setFilePath("/Users/aathithyamj/IdeaProjects/CsvToJson/src/main/java/com/striim/SampleData/bank1.csv");
        file1.setDelimiter(",");
        reader1.setFileConfig(file1);
        converter.addReader(reader1);
    }

    @ParameterizedTest
    @CsvSource({"/Users/aathithyamj/IdeaProjects/CsvToJson/src/main/java/com/striim/SampleData/bank.csv,true","/Users/aathithyamj/IdeaProjects/CsvToJson/src/main/java/com/striim/SampleData/bank1.csv,false"})
    void validateAddReader(String filepath,String expected) {
        int initialSize=converter.readers.size();
        Reader reader2=new CsvReader();
        FileConfig fileObj=new FileConfig();
        fileObj.setFilePath(filepath);
        fileObj.setDelimiter(",");
        reader2.setFileConfig(fileObj);
        converter.addReader(reader2);
        Assertions.assertEquals(Boolean.parseBoolean(expected),converter.readers.size()==initialSize+1);
    }

}