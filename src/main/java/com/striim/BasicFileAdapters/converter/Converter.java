package com.striim.BasicFileAdapters.converter;

import com.striim.BasicFileAdapters.UserInterface.UserInterface;
import com.striim.BasicFileAdapters.database.StorageSpace;
import com.striim.BasicFileAdapters.reader.Reader;
import com.striim.BasicFileAdapters.writer.Writer;
import lombok.Setter;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@XSlf4j(topic = "general")
@Component("converter")
public class Converter {

    private final ArrayList<Reader> readers;
    private final ArrayList<Writer> writers;
    private StorageSpace storage;
    private @Setter UserInterface userInterface;
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
        log.info("UserInterface selected : {}", userInterface.getClassName());

        this.storage = (StorageSpace) context.getBean(userInterface.getStorageType());
        log.info("Storage Space : {}", storage.getClassName());

        addReaders();
        log.info("No. of Readers added : {}", readers.size());

        readFiles(executorService);

        writers();
        log.info("No. of Writers added : {}", writers.size());

        executorService.shutdown();

        userInterface.print("Executor has been shutdown");
        log.info("The Executor has been Shutdown\n\n");

    }

    public void addReader(Reader reader) {
        if (readers.stream().noneMatch(e -> e.getFilePath().equals(reader.getFilePath()))) {
            readers.add(reader);

            log.info("{} is added", reader.getName());
        }
    }

    public boolean addWriter(Writer writer) {
        if (writers.stream().noneMatch(e -> e.getFilePath().equals(writer.getFilePath()))) {
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
            writer.setUserInterface(userInterface);
            if (addWriter(writer))
                writer.writeFile(writer, storage, executorService);
        });
    }

    public void readFiles(ExecutorService executorService) {
        List<Future<?>> futures = readers.stream().map(e-> executorService.submit(() -> storage.addDataObjects(e.readFile()))).collect(Collectors.toList());
        boolean loop = true;
        long start = System.nanoTime();
        userInterface.print("Files are being read .");
        while(loop) {
            loop = !futures.stream().allMatch(Future::isDone);
        }
        userInterface.print("File reading completed : time taken -> " + (System.nanoTime() - start) / 1000000 + " ms");
    }

}
