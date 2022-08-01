package com.striim.BasicFileAdapters.UserInterface;

import com.striim.BasicFileAdapters.converter.FileConfig;
import com.striim.BasicFileAdapters.database.DataRecord;
import com.striim.BasicFileAdapters.query.FilterFactory;
import com.striim.BasicFileAdapters.query.QueryEngine;
import lombok.extern.slf4j.XSlf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@XSlf4j(topic = "General")
@Component
public class XMLInterface extends UserInterface {

    private Scanner scanner;
    private String path;
    private String storageType;
    private Document document;
    private ArrayList<FileConfig> readers;
    private ArrayList<FileConfig> writers;
    private ConsoleInterface consoleInterface;

    public XMLInterface() {
        readConfigs();
    }

    @Autowired
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    @Autowired
    public void setConsoleInterface(ConsoleInterface consoleInterface) {
        this.consoleInterface = consoleInterface;
    }

    @Override
    public ArrayList<FileConfig> getReaderFileConfigs() {
        return readers;
    }

    @Override
    public ArrayList<FileConfig> getWriterFileConfigs() {
        return writers;
    }

    @Override
    public FileConfig getReaderFileConfig() {
        return consoleInterface.getReaderFileConfig();
    }

    @Override
    public FileConfig getWriterFileConfig() {
        return consoleInterface.getWriterFileConfig();
    }

    @Override
    public void generateQueries(FileConfig fileConfig, ArrayList<String> keySet) {
        List<Node> nodes = document.selectNodes("/Config/Writers");

        Optional<Node> tempNode = nodes.stream()
                .filter(n -> fileConfig.getFilePath().equalsIgnoreCase(n.selectSingleNode("FilePath").getStringValue())).findFirst();

        tempNode.ifPresent(node -> node.selectNodes("/Queries").forEach(e -> generateQuery(fileConfig, e, keySet)));
    }

    @Override
    public void fetchColumns(FileConfig fileConfig, ArrayList<String> keyset) {

        Optional<Node> tempNode = document.selectNodes("/Config/Writers").stream()
                .filter(n -> fileConfig.getFilePath().equalsIgnoreCase(n.selectSingleNode("FilePath").getStringValue()))
                .findFirst();
        if (tempNode.isPresent()) {
            List<Node> columns = tempNode.get().selectNodes("/Columns");
            if (columns.size() == 0)
                fileConfig.setFetchColumns(keyset);
            else {
                fileConfig.setFetchColumns((ArrayList<String>) columns.stream().map(Node::getStringValue)
                        .filter(keyset::contains).collect(Collectors.toList()));
            }
        }
    }

    @Override
    public String getStorageType() {
        return storageType == null ? "InMemoryDatabase" : storageType;
    }

    @Override
    public String getClassName() {
        return "XMLInterface";
    }

    @Override
    public void print(String msg) {
        System.out.println(msg);
    }

    private String verifyFilePath(String path, String type) {
        try {
            File f = new File(path);
            String dir = path.substring(0, path.lastIndexOf("/") + 1);
            File directory = new File(dir);
            if (directory.isDirectory()) {
                if (type.equals("Reader") && f.isFile() && f.canRead()) {
                    return path;
                }
                if (type.equals("Writer") && ((f.isFile() && f.canWrite()) || f.createNewFile()))
                    return path;
            }
            return null;
        } catch (IOException e) {
            print("File Error");
            log.warn("Error in opening the file -- {}", path);
            return null;
        }
    }

    private void setFileType(FileConfig fileConfig) {
        int index = fileConfig.getFilePath().lastIndexOf(".") + 1;
        fileConfig.setFileType(fileConfig.getFilePath().substring(index).toUpperCase());
    }

    private FileConfig prepareReaderFileConfig(Node e) {
        FileConfig fileConfig = new FileConfig();
        String path = verifyFilePath(e.selectSingleNode("FilePath").getStringValue(), "Reader");
        if (path == null)
            return null;
        fileConfig.setFilePath(path);
        setFileType(fileConfig);
        fileConfig.setType(fileConfig.getFileType() + "Reader");
        Node delim = e.selectSingleNode("Delimiter");
        fileConfig.setDelimiter(delim == null ? null : delim.getStringValue());
        return fileConfig;
    }

    private FileConfig prepareWriterFileConfig(Node e) {
        FileConfig fileConfig = new FileConfig();
        String path = verifyFilePath(e.selectSingleNode("FilePath").getStringValue(), "Writer");
        if (path == null)
            return null;
        fileConfig.setFilePath(path);
        setFileType(fileConfig);
        fileConfig.setType(fileConfig.getFileType() + "Writer");
        Node delim = e.selectSingleNode("Delimiter");
        fileConfig.setDelimiter(delim == null ? null : delim.getStringValue());
        return fileConfig;
    }

    private void readFilePath() {
        String path;
        print("Enter the XML file Path: ");
        while (this.path != null) {
            try {
                path = scanner.nextLine().trim();
                if (path.length() == 0 || path.equals(";")) {
                    if (this.path == null)
                        throw new NoSuchElementException("Enter the file path for xml");
                }
                path = verifyFilePath(path, "Reader");
                if (path != null)
                    this.path = path;

            } catch (NoSuchElementException e) {
                print("No path entered" + e.getMessage());

            } catch (IllegalStateException e) {
                print("Scanner closed");
                log.error("Scanner closed");
            }
            print("Enter proper file path: ");
        }
    }

    private void setStorageType() {
        List<Node> nodes = document.selectNodes("/Config");
        if (nodes.size() != 1) {
            storageType = "INMEMORYDATABASE";
            log.warn("Improper configuration of the storage type. Defaulting to InMemory Database");
        } else {
            storageType = nodes.get(0).getStringValue();
            log.info("Storage space configuration is set : {}", storageType);
        }
    }

    private void generateQuery(FileConfig fileConfig, Node e, ArrayList<String> keySet) {
        if (QueryEngine.isProperConstraint(e.getStringValue(), keySet)) {
            if (fileConfig.getQuery() == null) {
                fileConfig.setQuery(FilterFactory.getFilter(e.getStringValue()));
            } else {
                Predicate<DataRecord> temp = FilterFactory.getFilter(e.getStringValue());
                fileConfig.setQuery(temp);
            }
        }
    }

    private void setReaders() {
        readers = new ArrayList<>();

        List<Node> nodes = document.selectNodes("/Config/Readers");
        if (nodes.size() == 0) {
            print("No Reader Configuration is given");
            log.warn("No Reader Configuration is given");
            log.info("New XML File is requested");

            this.path = null;
            readConfigs();
        }

        readers = (ArrayList<FileConfig>) nodes.stream().map(this::prepareReaderFileConfig).filter(Objects::nonNull).collect(Collectors.toList());
        log.info("No. of Reader File Config : {}", readers.size());

    }

    private void setWriters() {
        writers = new ArrayList<>();

        List<Node> nodes = document.selectNodes("/Config/Writers");

        if (nodes.size() == 0) {
            print("No Writer Configuration is given");
            log.warn("No Writer Configuration is given");
            log.info("New XML File is requested");

            this.path = null;
            readConfigs();
        }

        writers = (ArrayList<FileConfig>) nodes.stream().map(this::prepareWriterFileConfig).filter(Objects::nonNull).collect(Collectors.toList());
        log.info("No. of Writers File Config : {}", writers.size());

    }

    private void readConfigs() {
        if (path == null)
            readFilePath();

        try {
            document = new SAXReader().read(new File(path));

            setStorageType();

            setReaders();

            setWriters();

        } catch (DocumentException e) {
            print("Error in reading a file");
        }
    }
}
