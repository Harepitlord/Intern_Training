package com.striim.BasicFileAdapters.UserInterface;

import com.striim.BasicFileAdapters.converter.FileConfig;

import java.util.ArrayList;

public interface UserInterface {

    public UserInterface getInterface();

    public void prepareFileConfigs();

    public ArrayList<FileConfig> getFileConfigs();
}
