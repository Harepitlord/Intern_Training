package com.striim.BasicFileAdapters.UserInterface;

import com.striim.BasicFileAdapters.converter.FileConfig;
import com.striim.BasicFileAdapters.database.DataRecord;

import java.util.ArrayList;
import java.util.function.Predicate;

public interface UserInterface {

    UserInterface getInterface();

    void prepareReaderFileConfigs();

    void prepareWriterFileConfigs();

    ArrayList<FileConfig> getReaderFileConfigs();

    ArrayList<FileConfig> getWriterFileConfigs();

    FileConfig getReaderFileConfig();

    FileConfig getWriterFileConfig();

    Predicate<DataRecord> generateQueries();

    ArrayList<String> fetchColumns(ArrayList<String> keyset);

    String getStorageType();

    String getClassName();

}
