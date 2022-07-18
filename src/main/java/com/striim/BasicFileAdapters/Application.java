package com.striim.BasicFileAdapters;

import com.striim.BasicFileAdapters.converter.Converter;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application {

    private final Scanner sc;
    private final Converter converter;
    private final ExecutorService executorService;

    public Application() {
        sc = new Scanner(System.in);
        converter = new Converter(sc);
        this.executorService = Executors.newFixedThreadPool(3);
    }

    public void runApp() {

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
        Application r = new Application();
        r.runApp();
    }
}

