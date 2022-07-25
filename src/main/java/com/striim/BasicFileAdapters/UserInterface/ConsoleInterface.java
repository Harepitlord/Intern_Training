package com.striim.BasicFileAdapters.UserInterface;

import com.striim.BasicFileAdapters.converter.FileConfig;
import com.striim.BasicFileAdapters.database.DataRecord;
import com.striim.BasicFileAdapters.query.FilterFactory;
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
import java.util.function.Predicate;

@Component
public class ConsoleInterface implements UserInterface {

    private Scanner scanner;
    private ArrayList<FileConfig> readerConfigs;
    private ArrayList<FileConfig> writerConfigs;

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

    public String getStorageType() {
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
        System.out.printf("Enter the details of the %s%n",type);
        while (next) {
            FileConfig fileConfig = new FileConfig();

            next = filePathInput(fileConfig);

            fileConfig.setType(fileConfig.getFileType() + type);

            temp.add(fileConfig);
        }
        return temp;
    }

    public Predicate<DataRecord> generateQueries() {

        Predicate<DataRecord> query = null;

        System.out.println("Enter the queries end with ',' to add more constraint and ';' to end the query");
        System.out.println("eg : col > val,col < val;");
        boolean another = true;
        do {
            String input = scanner.nextLine();
            if (input.length() == 0 || input.equals(";"))
                return null;
            if (input.endsWith(";")) {
                another = false;
                input = input.substring(0, input.length() - 1);
            }
            String[] constraints = input.split(",");
            for (String constraint : constraints) {
                if (query == null)
                    query = FilterFactory.getFilter(constraint);
                else {
                    Predicate<DataRecord> temp = FilterFactory.getFilter(constraint);
                    if (temp != null)
                        query = query.and(temp);
                }
            }
        } while (another);
        return query;
    }

    public ArrayList<String> fetchColumns(ArrayList<String> keySet) {
        System.out.println("Enter the names of the columns you want to fetch.Enter End when you want to end.For All Columns enter End.");
        System.out.println(keySet);
        ArrayList<String> fetchColumnsSet = new ArrayList<>();
        String colName = "Start";
        while (!colName.equalsIgnoreCase("END")) {
            colName = scanner.nextLine().trim();
            boolean colfound = false;
            for (String key : keySet) {
                if (key.equals(colName) || colName.equalsIgnoreCase("END")) {
                    colfound = true;
                    break;
                }
            }
            if (colfound) {
                fetchColumnsSet.add(colName);
            } else {
                System.out.println("Enter the column name correctly !!!");
            }
        }
        return fetchColumnsSet;
    }
}
