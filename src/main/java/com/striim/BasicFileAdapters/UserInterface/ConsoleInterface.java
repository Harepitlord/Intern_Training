package com.striim.BasicFileAdapters.UserInterface;

import com.striim.BasicFileAdapters.converter.FileConfig;
import com.striim.BasicFileAdapters.database.DataRecord;
import com.striim.BasicFileAdapters.database.StorageSpace;
import com.striim.BasicFileAdapters.query.FilterFactory;
import com.striim.BasicFileAdapters.query.QueryEngine;
import com.striim.BasicFileAdapters.reader.Reader;
import com.striim.BasicFileAdapters.writer.Writer;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Predicate;

@XSlf4j
@Component("ConsoleInterface")
public class ConsoleInterface extends UserInterface {

    private Scanner scanner;
    private ArrayList<FileConfig> readerConfigs;
    private ArrayList<FileConfig> writerConfigs;
    private ArrayList<FileConfig> temp;

    @Autowired
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
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
        prepareReaderFileConfigs();
        return readerConfigs;
    }

    @Override
    public ArrayList<FileConfig> getWriterFileConfigs() {
        prepareWriterFileConfigs();
        return writerConfigs;
    }

    @Override
    public FileConfig getReaderFileConfig() {
        return getFileConfig("Reader");
    }

    public String getStorageType() {
        System.out.println("Enter the storage type : "+ StorageSpace.getAvailableStorageSpaces());
        while (true) {
            int choice;
            try {
                String s = scanner.nextLine().trim();
                choice = Integer.parseInt(s);
            } catch (NumberFormatException ex) {
                System.out.println("Improper choice");
                continue;
            } catch (NoSuchElementException e) {
                System.out.println("No choice entered");
                continue;
            }
            if (choice == 1) {
                return "INMEMORYDATABASE";
            } else {
                System.out.println("Improper choice");
            }
        }
    }

    @Override
    public String getClassName() {
        return "ConsoleInterface";
    }

    @Override
    public FileConfig getWriterFileConfig() {
        return getFileConfig("Writer");
    }

    @Override
    public void print(String msg) {
        System.out.println(msg);
    }

    public void fileSpecificConfig(FileConfig fileConfig) {
        if (fileConfig.getFileType().equals("CSV")) {
            System.out.println("Enter the delimiter : (default => ',')");
            char delim = ',';
            String val = scanner.nextLine().trim();
            if (val.length() >= 1)
                delim = val.charAt(0);
            fileConfig.setDelimiter(String.valueOf(delim));
        }
    }

//    private int filePathInput(FileConfig fileConfig, String type) {
//        String path = "";
//        if (type.equals("Reader")) {
//            System.out.println("Supported readers are : " + Reader.getAvailableReaders());
//        } else {
//            System.out.println("Supported writers are : " + Writer.getAvailableWriters());
//        }
//        System.out.println("Enter the file Path: ");
//        int next = 1;
//        while (true) {
//            try {
//                path = scanner.nextLine().trim();
//                if (path.length() == 0 || path.equals(";")) {
//                    if (temp.size() > 0)
//                        return 0;
//                    else
//                        throw new NoSuchElementException("Empty Input line");
//                }
//                if (path.endsWith(";")) {
//                    next = -1;
//                    path = path.substring(0, path.length() - 1);
//                }
//                File f = new File(path);
//                String dir = path.substring(0, path.lastIndexOf("/") + 1);
//                File directory = new File(dir);
//                if (directory.isDirectory()) {
//                    if (type.equals("Writer")) {
//                        if (path.contains(".") && Writer.isAvailable(path)) {
//                            if (f.createNewFile()) {
//                                fileConfig.setFilePath(path);
//                                setFileType(fileConfig);
//                                break;
//                            }
//                        }
//                    } else if (type.equals("Reader") && f.isFile() && Reader.isAvailable(path)) {
//                        fileConfig.setFilePath(path);
//                        setFileType(fileConfig);
//                        break;
//                    }
//                }
//            } catch (NoSuchElementException e) {
//                System.out.println("No path entered" + e.getMessage());
//
//            } catch (IllegalStateException e) {
//                System.out.println("Scanner closed");
//                log.error("Scanner closed");
//                return -1;
//            } catch (IOException e) {
//                System.out.println("File Error");
//                log.warn("Error in opening the file -- {}", path);
//            }
//            System.out.println("Enter proper file path: ");
//        }
//        fileSpecificConfig(fileConfig);
//        return next;
//    }

    public boolean verifyFilePath(String path, String type) {
        if (path == null)
            return false;
        try {
            File f = new File(path);
            String dir = path.substring(0, path.lastIndexOf("/") + 1);
            File directory = new File(dir);
            if (directory.isDirectory()) {
                if (type.equals("Reader") && f.isFile() && f.canRead() && Reader.isAvailable(path)) {
                    return true;
                }
                if (type.equals("Writer") && ((f.isFile() && f.canWrite()) || f.createNewFile()) && Writer.isAvailable(path))
                    return true;
            }
            return false;
        } catch (IOException e) {
            print("File Error");
            log.warn("Error in opening the file -- {}", path);
            return false;
        }
    }

    private int filePathInput(FileConfig fileConfig, String type) {
        String path = "";
        if (type.equals("Reader")) {
            print("Supported readers are : " + Reader.getAvailableReaders());
        } else {
            print("Supported writers are : " + Writer.getAvailableWriters());
        }
        print("Enter the file Path: ");
        int next = 1;
        while (true) {
            try {
                path = scanner.nextLine().trim();
                if (path.length() == 0 || path.equals(";")) {
                    if (temp.size() > 0)
                        return 0;
                    else
                        throw new NoSuchElementException("Empty Input line");
                }
                if (path.endsWith(";")) {
                    next = -1;
                    path = path.substring(0, path.length() - 1);
                }
                boolean verified = verifyFilePath(path,type);
                if(verified) {
                    fileConfig.setFilePath(path);
                    setFileType(fileConfig);
                    break;
                }
            } catch (NoSuchElementException e) {
                print("No path entered" + e.getMessage());

            } catch (IllegalStateException e) {
                print("Scanner closed");
                log.error("Scanner closed");
                return -1;
            }
            print("Enter proper file path: ");
        }
        fileSpecificConfig(fileConfig);
        return next;
    }

    protected void setFileType(FileConfig fileConfig) {
        int index = fileConfig.getFilePath().lastIndexOf(".") + 1;
        fileConfig.setFileType(fileConfig.getFilePath().substring(index).toUpperCase());
    }

    private FileConfig getFileConfig(String type) {
        FileConfig fileConfig = new FileConfig();

        filePathInput(fileConfig, type);

        fileConfig.setType(fileConfig.getFileType() + type);

        return fileConfig;
    }

    private ArrayList<FileConfig> prepareFileConfigs(String type) {
        int next = 1;
        temp = new ArrayList<>();
        System.out.printf("Enter the details of the %s : End with ';' to end the input \n", type);
        while (next == 1) {
            FileConfig fileConfig = new FileConfig();

            next = filePathInput(fileConfig, type);

            if (next != 0) {
                fileConfig.setType(fileConfig.getFileType() + type);

                temp.add(fileConfig);
            }
        }
        return temp;
    }

    public Predicate<DataRecord> generateQueries(ArrayList<String> keySet) {

        Predicate<DataRecord> query = null;

        System.out.println("Enter the queries end with ',' to add more constraint and ';' to end the query");
        System.out.println("eg : col > val,col < val;");
        System.out.println("Available Columns are : "+keySet);
        System.out.println("Supported operations are : "+FilterFactory.getAvailableOperation());
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
                if(QueryEngine.isProperConstraint(constraint,keySet)) {
                    if (query == null)
                        query = FilterFactory.getFilter(constraint);
                    else {
                        Predicate<DataRecord> temp = FilterFactory.getFilter(constraint);
                        if (temp != null)
                            query = query.and(temp);
                    }
                } else{
                    System.out.println("Enter the constraints properly");
                    query=null;
                    another=true;
                    break;
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
            boolean columnFound = false;
            for (String key : keySet) {
                if (key.equals(colName) || colName.equalsIgnoreCase("END")) {
                    columnFound = true;
                    break;
                }
            }
            if (columnFound) {
                fetchColumnsSet.add(colName);
            } else {
                System.out.println("Enter the column name correctly !!!");
            }
        }
        return fetchColumnsSet;
    }
}
