package com.striim.BasicFileAdapters.UserInterface;

import com.striim.BasicFileAdapters.converter.FileConfig;
import com.striim.BasicFileAdapters.database.DataRecord;
import com.striim.BasicFileAdapters.reader.Reader;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Predicate;

@XSlf4j(topic = "General")
@Component
public class XMLInterface extends UserInterface {

    private Scanner scanner;

    private String path;

    @Autowired
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public UserInterface getInterface() {
        return new XMLInterface();
    }

    @Override
    public void prepareReaderFileConfigs() {

    }

    @Override
    public void prepareWriterFileConfigs() {

    }

    @Override
    public ArrayList<FileConfig> getReaderFileConfigs() {
        return null;
    }

    @Override
    public ArrayList<FileConfig> getWriterFileConfigs() {
        return null;
    }

    @Override
    public FileConfig getReaderFileConfig() {
        return null;
    }

    @Override
    public FileConfig getWriterFileConfig() {
        return null;
    }

    @Override
    public Predicate<DataRecord> generateQueries(ArrayList<String> keySet) {
        return null;
    }

    @Override
    public ArrayList<String> fetchColumns(ArrayList<String> keyset) {
        return null;
    }

    @Override
    public String getStorageType() {
        return null;
    }

    @Override
    public String getClassName() {
        return null;
    }

    private void getFilePath() {
        String path;
        System.out.println("Enter the XML file Path: ");
        while (true) {
            try {
                path = scanner.nextLine().trim();
                if (path.length() == 0 || path.equals(";")) {
                    if (this.path == null)
                        throw new NoSuchElementException("Enter the file path for xml");
                }
                if (path.endsWith(";")) {
                    path = path.substring(0, path.length() - 1);
                }
                File f = new File(path);
                String dir = path.substring(0, path.lastIndexOf("/") + 1);
                File directory = new File(dir);
                if (directory.isDirectory()) {
                    if (f.isFile() && Reader.isAvailable(path)) {
                        this.path = path;
                        break;
                    }
                }
            } catch (NoSuchElementException e) {
                System.out.println("No path entered" + e.getMessage());

            } catch (IllegalStateException e) {
                System.out.println("Scanner closed");
                log.error("Scanner closed");
            }
            System.out.println("Enter proper file path: ");
        }
    }
}
