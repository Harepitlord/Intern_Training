package com.striim.BasicFileAdapters.UserInterface;

import com.striim.BasicFileAdapters.converter.FileConfig;
import com.striim.BasicFileAdapters.database.DataRecord;
import com.striim.BasicFileAdapters.query.FilterFactory;
import com.striim.BasicFileAdapters.query.QueryEngine;
import com.striim.BasicFileAdapters.reader.Reader;
import com.striim.BasicFileAdapters.writer.Writer;
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
@Component("XMLInterface")
public class XMLInterface extends UserInterface {

    private Scanner scanner;
    private String xmlFilePath;
    private String storageType;
    private Document document;
    private ArrayList<FileConfig> readers;
    private ArrayList<FileConfig> writers;
    private ConsoleInterface consoleInterface;

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
        if (xmlFilePath == null)
            readConfigs();
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
        List<Node> nodes = document.selectNodes("Config/Writers/Writer");

        Optional<Node> tempNode = nodes.stream()
                .filter(n -> fileConfig.getFilePath().equalsIgnoreCase(n.selectSingleNode("FilePath").getStringValue().trim())).findFirst();

        tempNode.ifPresent(node -> node.selectNodes("Queries/Query").forEach(e -> generateQuery(fileConfig, e, keySet)));
    }

    @Override
    public void fetchColumns(FileConfig fileConfig, ArrayList<String> keyset) {

        Optional<Node> tempNode = document.selectNodes("Config/Writers/Writer").stream()
                .filter(n -> fileConfig.getFilePath().equalsIgnoreCase(n.selectSingleNode("FilePath").getStringValue().trim()))
                .findFirst();
        if (tempNode.isPresent()) {
            List<Node> columns = tempNode.get().selectNodes("Columns/Column");
            if (columns.size() == 0)
                fileConfig.setFetchColumns(keyset);
            else {
                fileConfig.setFetchColumns((ArrayList<String>) columns.stream().map(n->n.getStringValue().trim())
                        .filter(keyset::contains).collect(Collectors.toList()));
            }
        }
    }

    @Override
    public String getStorageType() {
        if (xmlFilePath == null)
            readConfigs();
        return storageType == null ? "INMEMORYDATABASE" : storageType;
    }

    @Override
    public String getClassName() {
        return "XMLInterface";
    }

    @Override
    public void print(String msg) {
        System.out.println(msg);
    }

    private void reconfigure(String msg) {
        //print(msg);
        log.warn(msg);
        log.info("New XML File is requested");

        this.xmlFilePath = null;
        readConfigs();
    }

    private String verifyFilePath(String path, String type) {
        if (path == null)
            return null;
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
        String path;
        try {
            path = verifyFilePath(e.selectSingleNode("FilePath").getStringValue().trim(), "Reader");
        } catch (NullPointerException exp) {
            log.warn("No File Path is given");
            log.warn(exp.getMessage());
            return null;
        }
        if (path == null || !Reader.isAvailable(path))
            return null;

        FileConfig fileConfig = new FileConfig();
        fileConfig.setFilePath(path);
        setFileType(fileConfig);
        fileConfig.setType(fileConfig.getFileType() + "Reader");
        Node delim = e.selectSingleNode("Delimiter");
        fileConfig.setDelimiter(delim == null ? null : delim.getStringValue());
        return fileConfig;
    }

    private FileConfig prepareWriterFileConfig(Node e) {
        FileConfig fileConfig = new FileConfig();
        String path;
        try {
            path = verifyFilePath(e.selectSingleNode("FilePath").getStringValue().trim(), "Writer");
        }catch (NullPointerException exp) {
            log.warn("No File Path is given");
            log.warn(exp.getMessage());
            return null;
        }
        if (path == null || !Writer.isAvailable(path))
            return null;
        fileConfig.setFilePath(path);
        setFileType(fileConfig);
        fileConfig.setType(fileConfig.getFileType() + "Writer");
        Node delim = e.selectSingleNode("Delimiter");
        fileConfig.setDelimiter(delim == null ? null : delim.getStringValue().trim());
        return fileConfig;
    }

    private void readFilePath() {
        String path;
        print("Enter the XML file Path: ");
        while (this.xmlFilePath == null) {
            try {
                path = scanner.nextLine().trim();
                if (path.length() == 0 || path.equals(";")) {
                    if (this.xmlFilePath == null)
                        throw new NoSuchElementException("Enter the file path for xml");
                }
                path = verifyFilePath(path, "Reader");
                if (path != null) {
                    this.xmlFilePath = path;
                    return;
                }

            } catch (NoSuchElementException e) {
                print("No path entered" + e.getMessage());

            } catch (IllegalStateException e) {
                print("Scanner closed");
                log.error("Scanner closed");
            }
            print("readFilePath : Enter proper file path: ");
            this.xmlFilePath = null;
        }
    }

    private void setStorageType() {
        List<Node> nodes = document.selectNodes("/Config/StorageType");
        if (nodes.size() != 1) {
            storageType = "INMEMORYDATABASE";
            log.warn("Improper configuration of the storage type. Defaulting to InMemory Database");
        } else {
            storageType = nodes.get(0).getStringValue().trim();
            log.info("Storage space configuration is set : {}", storageType);
        }
    }

    private void generateQuery(FileConfig fileConfig, Node e, ArrayList<String> keySet) {
        if (QueryEngine.isProperConstraint(e.getStringValue(), keySet)) {
            if (fileConfig.getQuery() == null) {
                fileConfig.setQuery(FilterFactory.getFilter(e.getStringValue().trim()));
            } else {
                Predicate<DataRecord> temp = FilterFactory.getFilter(e.getStringValue().trim());
                fileConfig.setQuery(temp);
            }
        }
    }

    private void setReaders() {
        readers = new ArrayList<>();

        List<Node> nodes = document.selectNodes("Config/Readers/Reader");
        if (nodes.size() == 0) {
            reconfigure("No Reader Configuration is given");
        }

        readers = (ArrayList<FileConfig>) nodes.stream().map(this::prepareReaderFileConfig).filter(Objects::nonNull).collect(Collectors.toList());
        if (readers.size() == 0)
            reconfigure("No Proper Reader Configuration is given");
        log.info("No. of Reader File Config : {}", readers.size());

    }

    private void setWriters() {
        writers = new ArrayList<>();

        List<Node> nodes = document.selectNodes("Config/Writers/Writer");

        if (nodes.size() == 0) {
            reconfigure("No Writer Configuration is given");
        }

        writers = (ArrayList<FileConfig>) nodes.stream().map(this::prepareWriterFileConfig).filter(Objects::nonNull).collect(Collectors.toList());
        if (writers.size()==0)
            reconfigure("No Proper Writer Configuration is given");
        log.info("No. of Writers File Config : {}", writers.size());

    }

    private void readConfigs() {
        if (xmlFilePath == null)
            readFilePath();

        try {
            document = new SAXReader().read(new File(xmlFilePath));

            setStorageType();

            setReaders();

            setWriters();

        } catch (DocumentException e) {
            print("Error in reading a file");
            log.error(e.getMessage());
        }
    }
}
