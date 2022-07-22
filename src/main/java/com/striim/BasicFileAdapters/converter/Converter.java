package com.striim.BasicFileAdapters.converter;

import com.striim.BasicFileAdapters.UserInterface.UserInterface;
import com.striim.BasicFileAdapters.database.InMemoryDatabase;
import com.striim.BasicFileAdapters.reader.Reader;
import com.striim.BasicFileAdapters.writer.Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Component
@Qualifier("converter")
public class Converter {

    private final ArrayList<Reader> readers;
    private final ArrayList<Writer> writers;
    private InMemoryDatabase database;
    @Autowired
    private Scanner scanner;
    private UserInterface userInterface;
    private ApplicationContext context;
    @Autowired
    private ExecutorService executorService;



    public Converter() {
        this.database = new InMemoryDatabase();
        this.readers = new ArrayList<>();
        this.writers = new ArrayList<>();
    }

    public void runApp(ApplicationContext context) {

        this.context = context;

        prepareUserInterface();

        addReaders();

        readFiles(executorService);

        writers();

        executorService.shutdown();

        System.out.println("Executor has been shutdown");

    }

    private void prepareUserInterface() {
        System.out.println("Enter the choice of input: 1.ConsoleInterface 2.XML Interface");
        while (true) {
            String s = scanner.nextLine().trim();
        }
    }

    public void addReader(Reader reader) {
        if (!readers.contains(reader)) {
            readers.add(reader);
        }
    }

    public boolean addWriter(Writer writer) {
        if (!writers.contains(writer)) {
            writers.add(writer);
            return true;
        }
        return false;
    }

    public void addReaders() {
        ArrayList<FileConfig> readersFileConfigs=userInterface.getReaderFileConfigs();
        readersFileConfigs.forEach(r -> readers.add((Reader)context.getBean(r.getType())));
    }

    public void writers() {
        ArrayList<FileConfig> writersFileConfigs=userInterface.getWriterFileConfigs();
        writersFileConfigs.forEach(w -> writers.add((Writer)context.getBean(w.getType())));
    }

    public void readFiles(ExecutorService executorService) {
        List<Future<?>> futures = readers.stream().map(e-> executorService.submit(() -> database.addDataObjects(e.readFile()))).collect(Collectors.toList());
        boolean loop = true;
        long start = System.nanoTime();
        System.out.print("Files are being read .");
        while(loop) {
            loop = !futures.stream().allMatch(Future::isDone);
        }
        System.out.println("File reading completed : time taken -> "+(System.nanoTime()-start));
    }
}
