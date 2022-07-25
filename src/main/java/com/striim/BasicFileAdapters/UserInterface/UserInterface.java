package com.striim.BasicFileAdapters.UserInterface;

import com.striim.BasicFileAdapters.converter.FileConfig;

import java.util.ArrayList;

public interface UserInterface {

    public UserInterface getInterface();

    public void prepareReaderFileConfigs();

    public void prepareWriterFileConfigs();

    public ArrayList<FileConfig> getReaderFileConfigs();

    public ArrayList<FileConfig> getWriterFileConfigs();

    public FileConfig getReaderFileConfig();

    public FileConfig getWriterFileConfig();

    public String setStorageType();

}
