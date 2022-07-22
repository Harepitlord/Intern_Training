package com.striim.BasicFileAdapters;

import com.striim.BasicFileAdapters.converter.Converter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@Qualifier("Application")
public class Application {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class);
        Converter app = (Converter) context.getBean("Converter");
        app.runApp(context);
    }
}

