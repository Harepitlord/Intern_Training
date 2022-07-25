package com.striim.BasicFileAdapters.UserInterface;

import com.striim.BasicFileAdapters.converter.FileConfig;
import com.striim.BasicFileAdapters.reader.CsvReader;
import com.striim.BasicFileAdapters.reader.Reader;
import com.striim.BasicFileAdapters.writer.JsonWriter;
import com.striim.BasicFileAdapters.writer.Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

@Component
public class ConsoleInterface implements UserInterface {

    private Scanner scanner;
    private ArrayList<FileConfig> readerConfigs;
    private ArrayList<FileConfig> writerConfigs;

    public static HashMap<String, Reader> readers = new HashMap<>();
    public static HashMap<String, Writer> writers = new HashMap<>();

    static {

        readers.put("CSV READER",new CsvReader());

        writers.put("JSON WRITER", new JsonWriter());
    }

    @Autowired
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    @Bean
    @Override
    public UserInterface getInterface() {
        return new ConsoleInterface();
    }

    @Override
    public void prepareReaderFileConfigs() {
        readerConfigs = new ArrayList<>();
        readerConfigs.addAll(prepareFileConfigs("Reader"));
    }

    @Override
    public void prepareWriterFileConfigs() {
        writerConfigs = new ArrayList<>();
        writerConfigs.addAll(prepareFileConfigs("Writer"));

    }

    @Override
    public ArrayList<FileConfig> getReaderFileConfigs() {
        return readerConfigs;
    }

    @Override
    public ArrayList<FileConfig> getWriterFileConfigs() {
        return writerConfigs;
    }

    @Override
    public FileConfig getReaderFileConfig() {
        return getFileConfig("Reader");
    }

    public String setStorageType(){
        System.out.println("Enter the storage type : ");
        return scanner.nextLine().trim().toUpperCase();
    }

    @Override
    public FileConfig getWriterFileConfig() {
        return getFileConfig("Writer");
    }

    public void fileSpecificConfig(FileConfig fileConfig) {
        if (fileConfig.getFileType().equals("CSV")) {
            System.out.println("Enter the delimiter : (default => ',')");
            char delim = ',';
            String val = scanner.nextLine();
            if (val.length() > 1)
                delim = val.trim().charAt(0);
            fileConfig.setDelimiter(String.valueOf(delim));
        }
    }

    private boolean filePathInput(FileConfig fileConfig) {
        String path;
        System.out.println("Enter The file Path: ");
        boolean next = true;
        while (true) {
            path = scanner.nextLine().trim();
            if (path.length() == 0 || path.equals(";")) {
                if (readerConfigs.size() > 0)
                    next = false;
                path = path.substring(0, path.length() - 1);
            }
            File f = new File(path);
            if (f.isFile()) {
                fileConfig.setFilePath(path);
                setFileTypeType(fileConfig);
                break;
            }
            System.out.println("Enter proper file path: ");
        }
        fileSpecificConfig(fileConfig);
        return next;
    }

    protected void setFileTypeType(FileConfig fileConfig) {
        int index = fileConfig.getFilePath().lastIndexOf(".");
        fileConfig.setFileType(fileConfig.getFilePath().substring(index).toUpperCase());
    }

    private FileConfig getFileConfig(String type) {
        FileConfig fileConfig = new FileConfig();
        filePathInput(fileConfig);

        fileConfig.setType(fileConfig.getFileType()+type);
        return fileConfig;
    }

    private ArrayList<FileConfig> prepareFileConfigs(String type) {
        boolean next = true;
        ArrayList<FileConfig> temp = new ArrayList<>();
        System.out.printf("Enter the details of the %s/n",type);
        while (next) {
            FileConfig fileConfig = new FileConfig();

            next = filePathInput(fileConfig);

            fileConfig.setType(fileConfig.getFileType()+type);

            temp.add(fileConfig);
        }
        return temp;
    }

}
