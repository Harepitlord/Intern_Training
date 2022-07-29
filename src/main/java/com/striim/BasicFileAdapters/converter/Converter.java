package com.striim.BasicFileAdapters.converter;

import com.striim.BasicFileAdapters.UserInterface.UserInterface;
import com.striim.BasicFileAdapters.database.StorageSpace;
import com.striim.BasicFileAdapters.reader.Reader;
import com.striim.BasicFileAdapters.writer.Writer;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@XSlf4j(topic = "general")
@Component("converter")
public class Converter {

    private final ArrayList<Reader> readers;
    private final ArrayList<Writer> writers;
    private StorageSpace storage;
    @Autowired
    private Scanner scanner;
    private UserInterface userInterface;
    private ApplicationContext context;
    @Autowired
    private ExecutorService executorService;


    public Converter() {
        this.readers = new ArrayList<>();
        this.writers = new ArrayList<>();
    }

    public void runApp(ApplicationContext context) {
        log.info("The converter pipeline is created.");

        this.context = context;

        prepareUserInterface();
        log.info("UserInterface selected : {}", userInterface.getClassName());

        this.storage = (StorageSpace) context.getBean(userInterface.getStorageType());
        log.info("Storage Space : {}", storage.getClassName());

        addReaders();
        log.info("No. of Readers added : {}", readers.size());

        readFiles(executorService);

        writers();
        log.info("No. of Writers added : {}", writers.size());

        executorService.shutdown();

        System.out.println("Executor has been shutdown");

    }

    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    private void prepareUserInterface() {
        if (userInterface != null)
            return;
        System.out.println("Enter the choice of input: "+UserInterface.getAvailableUserInterfaces());
        while (true) {
            String s = scanner.nextLine().trim();
            int choice;
            try{
                choice=Integer.parseInt(s);
            }
            catch(Exception ex){
                System.out.println("Improper choice");
                continue;
            }
            switch (choice) {
                case 1: {
                    this.userInterface = (UserInterface) context.getBean("ConsoleInterface");
                    return;
                }
//                case 2: {
//                    this.userInterface = (UserInterface) context.getBean("XMLInterface");
//                    return;
//                }
                default: {
                    System.out.println("Improper choice");
                }
            }
        }
    }

    public void addReader(Reader reader) {
        if (!readers.contains(reader)) {
            readers.add(reader);

            log.info("{} is added", reader.getName());
        }
    }

    public boolean addWriter(Writer writer) {
        if (!writers.contains(writer)) {
            writers.add(writer);

            log.info("{} is added", writer.getName());

            return true;
        }
        return false;
    }

    public void addReaders() {
        ArrayList<FileConfig> readersFileConfigs=userInterface.getReaderFileConfigs();
        readersFileConfigs.forEach(r -> {
            Reader reader = (Reader) context.getBean(r.getType());
            reader.setFileConfig(r);
            addReader(reader);
        });
    }

    public void writers() {
        ArrayList<FileConfig> writersFileConfigs = userInterface.getWriterFileConfigs();
        writersFileConfigs.forEach(w -> {
            Writer writer = (Writer) context.getBean(w.getType());
            writer.setFileConfig(w);
            if (addWriter(writer))
                writer.writeFile(userInterface, storage, executorService);
        });
    }

    public void readFiles(ExecutorService executorService) {
        List<Future<?>> futures = readers.stream().map(e-> executorService.submit(() -> storage.addDataObjects(e.readFile()))).collect(Collectors.toList());
        boolean loop = true;
        long start = System.nanoTime();
        System.out.println("Files are being read .");
        while(loop) {
            loop = !futures.stream().allMatch(Future::isDone);
        }
        System.out.println("File reading completed : time taken -> "+(System.nanoTime()-start));
    }

}
