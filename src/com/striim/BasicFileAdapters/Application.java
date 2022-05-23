package com.striim.BasicFileAdapters;

import com.striim.BasicFileAdapters.converter.Converter;

import java.util.Scanner;

public class Application {

    private final Scanner sc;

    private final Converter converter;

    public Application() {
        sc = new Scanner(System.in);
        converter = new Converter(sc);
    }

    public void runApp() {

        converter.addReaders();

        converter.readFiles();

        converter.writers();

    }

    protected void finalize() {
        this.sc.close();
    }

    public static void main(String[] args) {
        Application r = new Application();
        r.runApp();
    }
}

