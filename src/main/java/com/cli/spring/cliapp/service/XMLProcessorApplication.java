package com.cli.spring.cliapp.service;


import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ShellComponent
public class XMLProcessorApplication {
    private static final System.Logger logger = System.getLogger(XMLProcessorApplication.class.getName());

    @ShellMethod(key = "printFile", value = "Print the contents of the file")
    public void checkIfFileExists(@ShellOption(value = "--infile") String filePath){
        logger.log(System.Logger.Level.INFO, "Checking if file exists");
        Path xmlFile = Path.of(filePath);
        if(Files.isRegularFile(xmlFile)){
            try(BufferedReader reader = Files.newBufferedReader(xmlFile)){
                reader.lines().filter(s -> (!(s.startsWith("<?"))))
                        .forEach(s -> System.out.println(s));

            } catch (IOException ioe){
                logger.log(System.Logger.Level.ERROR, ioe.getMessage());
            }

        } else {
            if(Files.isDirectory(xmlFile)){
                logger.log(System.Logger.Level.ERROR, "The path provided is a directory. Please provide the absolute path of the file");
            } else {
                logger.log(System.Logger.Level.ERROR, "File does not exist. Please check the file path.");
                System.out.println("File does not exist in the path provided");
            }
        }
    }

}
