package com.striim.BasicFileAdapters.writer;

import com.striim.BasicFileAdapters.UserInterface.UserInterface;
import com.striim.BasicFileAdapters.converter.FileConfig;
import com.striim.BasicFileAdapters.database.DataRecord;
import com.striim.BasicFileAdapters.database.StorageSpace;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.XSlf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;

@XSlf4j(topic = "General")
public abstract class Writer {

    protected FileWriter fileWriter;
    protected String state;
    protected @Getter @Setter UserInterface userInterface;
    protected @Getter
    @Setter FileConfig fileConfig;

    private static final ArrayList<String> availableWriters;

    static {
        availableWriters = new ArrayList<>();
        availableWriters.add("JSONWRITER");
    }

    public static ArrayList<String> getAvailableWriters() {
        return availableWriters;
    }

    protected boolean prepareWriter() {
        try {
            File file = new File(fileConfig.getFilePath());
            fileWriter = new FileWriter(file, true);
            state = "File Connected";
            return true;
        } catch (IOException ex) {
            state = "File Not Found";
            return false;
        }
    }

    protected void errorHandling() {
        if ("File Not Found".equals(state)) {
            userInterface.print("There is no such file in the given path, Re-enter new File Path");

            log.warn("{} -- {} -- The file is not found", fileConfig.getType(), fileConfig.getFilePath());
        }
        fileConfig = userInterface.getWriterFileConfig();
    }

    public static boolean isAvailable(String path) {
        return getAvailableWriters().contains(path.substring(path.lastIndexOf(".") + 1).toUpperCase() + "WRITER");
    }

    public String getFilePath() {
        return fileConfig.getFilePath();
    }

    public String getName() {
        return String.format("%s -- %s", fileConfig.getType(), fileConfig.getFilePath());
    }

    public Predicate<DataRecord> getQuery() {
        return fileConfig.getQuery();
    }

    public ArrayList<String> getFetchColumns() {
        return fileConfig.getFetchColumns();
    }

    public abstract void writeFile(Writer writer, StorageSpace database, ExecutorService executorService);

}
