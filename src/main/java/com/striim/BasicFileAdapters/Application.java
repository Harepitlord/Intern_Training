package com.striim.BasicFileAdapters;

import com.striim.BasicFileAdapters.UserInterface.UserInterface;
import com.striim.BasicFileAdapters.converter.Converter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.NoSuchElementException;
import java.util.Scanner;

@SpringBootApplication
@Qualifier("Application")
public class Application {


    private static final Scanner scanner = AppConfig.getScanner();

    private static UserInterface prepareUserInterface(ApplicationContext context) {
        UserInterface userInterface;
        System.out.println("Enter the choice of input: " + UserInterface.getAvailableUserInterfaces());
        while (true) {
            int choice;
            try {
                String s = scanner.nextLine().trim();
                choice = Integer.parseInt(s);
            } catch (NumberFormatException ex) {
                System.out.println("Improper choice");
                continue;
            } catch (NoSuchElementException e) {
                System.out.println("Enter the choice");
                continue;
            }
            switch (choice) {
                case 1: {
                    userInterface = (UserInterface) context.getBean("ConsoleInterface");
                    return userInterface;
                }
                case 2: {
                    userInterface = (UserInterface) context.getBean("XMLInterface");
                    return userInterface;
                }
                default: {
                    System.out.println("Improper choice");
                }
            }
        }

    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class);
        Converter app = (Converter) context.getBean("converter");
        app.setUserInterface(prepareUserInterface(context));
        app.runApp(context);
    }
}

