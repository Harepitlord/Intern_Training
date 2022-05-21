package com.striim.BasicFileAdapters;

import com.striim.BasicFileAdapters.converter.Converter;

import java.util.Scanner;

public class Application {

    // The scanner object to get user inputs
    private final Scanner sc;

    // This object is to handle the entire process
    private final Converter converter;

    public Application() {
        sc = new Scanner(System.in);
        converter = new Converter(sc);
    }

    // This function acts as the central one where others are called
    public void runner() {

        System.out.println("Enter the ");

        converter.addReaders(1);

        converter.convert();

        this.cleanUp();
    }

    // This function closes the opened files
    private void cleanUp() {
        this.converter.cleanUp();
        this.sc.close();
    }

    public static void main(String[] args) {
        Application r = new Application();
        r.runner();
    }
}

