package com.striim.BasicFileAdapters;

import com.striim.BasicFileAdapters.converter.Converter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AppConfig {

    private Scanner scanner;
    private Converter converter;
    private ExecutorService executorService;

    @Bean("Scanner")
    public Scanner getScanner() {
        if (scanner == null)
            scanner = new Scanner(System.in);

        return scanner;
    }

    @Bean("Converter")
    public Converter getConverter() {
        if (converter == null)
            converter = new Converter();

        return converter;
    }

    @Bean("ExecutorService")
    public ExecutorService getExecutorService() {
        if (executorService == null)
            executorService = Executors.newFixedThreadPool(3);

        return executorService;
    }

}
