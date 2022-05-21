package com.cli.spring.cliapp.service;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.logging.Level;
import java.util.logging.Logger;

@ShellComponent
public class ShellApplication {
    private static Logger logger = Logger.getLogger(ShellApplication.class.getName());

    {
        System.out.println("Welcome to the Shell Application. Please type 'help' for options");
    }

    @ShellMethod(value = "Greet someone.", key = "greet")
    public String remoteGreet(@ShellOption(value = "-s", arity = 2) String [] name){
        logger.log(Level.INFO, String.format("Starting the application::%s", ShellApplication.class.getName()));
        return "Hello " +name[0] + " " +name[1];
    }

    @ShellMethod(key = "greetM", value = "Greet multiple names")
    public String greetMulti(@ShellOption(value = "--gname") String gName,
                             @ShellOption(value = "--fname") String fName){
        return String.format("Hello %s %s", gName, fName);
    }
}
