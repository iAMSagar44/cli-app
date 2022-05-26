package com.cli.spring.cliapp.service;


import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ShellComponent
public class XMLProcessorApplication {
    private static final System.Logger logger = System.getLogger(XMLProcessorApplication.class.getName());

    @ShellMethod(key = "printFile", value = "Print the contents of the file")
    public void processXML(@ShellOption(value = "--infile") String filePath){
        Path xmlFile = Path.of(filePath);
        checkIfFileExists(xmlFile);

        DocumentBuilder documentBuilder;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(xmlFile.toFile());
            document.getDocumentElement().normalize();

            logger.log(System.Logger.Level.INFO, "Reading the XML payload");

            Node mainNode = document.getElementsByTagName("notificationDetails").item(0);
            NodeList childNodeList = mainNode.getChildNodes();
            Node current;
            for(int i=0; i <childNodeList.getLength(); i++){
                current = childNodeList.item(i);
                if (current.getNodeType() == Node.ELEMENT_NODE){
                    System.out.println(current.getNodeName() + "::" +
                            current.getTextContent());
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }

    private void checkIfFileExists(Path filePath) {
        logger.log(System.Logger.Level.INFO, "Checking if file exists");
        if(Files.isRegularFile(filePath)){
            try(BufferedReader reader = Files.newBufferedReader(filePath)){
                reader.lines().filter(s -> (!(s.startsWith("<?"))))
                        .forEach(s -> System.out.println(s));


            } catch (IOException ioe){
                logger.log(System.Logger.Level.ERROR, ioe.getMessage());
            }

        } else {
            if(Files.isDirectory(filePath)){
                logger.log(System.Logger.Level.ERROR, "The path provided is a directory. Please provide the absolute path of the file");
            } else {
                logger.log(System.Logger.Level.ERROR, "File does not exist. Please check the file path.");
                System.out.println("File does not exist in the path provided");
            }
        }

    }

}
