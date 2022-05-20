package com.striim.BasicFileAdapters;

import com.striim.BasicFileAdapters.converter.Converter;

import java.util.Scanner;

public class Application {

    // The scanner object to get user inputs
    private final Scanner sc;

    // This object is to handle the entire process
    private final Converter fileFormatConvertor;

    public Application() {
        sc = new Scanner(System.in);
        fileFormatConvertor = new Converter(sc);
    }

    // This function acts as the central one where others are called
    public void runner() {

        System.out.println("Enter the number of File inputs : ");
        int n = Integer.parseInt(sc.nextLine());
        fileFormatConvertor.addReaders(n);

        System.out.println("Enter the number of File Outputs: ");
        n = Integer.parseInt(sc.nextLine());
        fileFormatConvertor.addWriters(n);

        fileFormatConvertor.convert();

        this.cleanUp();
    }

    // This function closes the opened files
    private void cleanUp() {
        this.fileFormatConvertor.cleanUp();
        this.sc.close();
    }

    public static void main(String[] args) {
        Application r = new Application();
        r.runner();
    }
}

