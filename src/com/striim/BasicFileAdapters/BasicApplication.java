package com.striim.BasicFileAdapters;

import com.fasterxml.jackson.databind.cfg.ConfigOverride;
import com.striim.BasicFileAdapters.converter.Converter;
import com.striim.BasicFileAdapters.database.Database;
import com.striim.BasicFileAdapters.database.InMemoryDatabase;
import com.striim.KafkaFileAdapters.KafkaDatabase;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BasicApplication {

    private final Scanner sc;
    private final ExecutorService executorService;

    public BasicApplication() {
        sc = new Scanner(System.in);
        System.out.println();
        this.executorService = Executors.newFixedThreadPool(3);
    }

    public void runApp() {

        System.out.println("Enter The storage type: 1. InMemory Database 2. Kafka ");
        Database database;
        switch (Integer.parseInt(sc.nextLine())) {
            case 1 : {
                database = new InMemoryDatabase();
                break;
            }
            case 2 : {
                database = new KafkaDatabase();
                break;
            }
            default:
                System.out.println("Defaulting to InMemory database");
                database = new InMemoryDatabase();
        }

        Converter converter = new Converter(sc,database);

        converter.addReaders();

        converter.readFiles(executorService);

        converter.writers(executorService);

        executorService.shutdown();

        System.out.println("Executor has been shutdown");

    }

    protected void finalize() {
        this.sc.close();
    }

    public static void main(String[] args) {
        BasicApplication r = new BasicApplication();
        r.runApp();
    }
}

