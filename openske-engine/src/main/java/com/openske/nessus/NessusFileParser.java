package com.openske.nessus;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.openske.model.software.Vulnerability;


public class NessusFileParser {
    
    private File nessusFile;
    private DocumentBuilderFactory documentFactory;
    private DocumentBuilder documentBuilder;
    private Document document;
    private XPathFactory xpathFactory;
     
    
    public NessusFileParser() {
        initialize();
    }
    
    private void initialize() {
        try {
            if(nessusFile.exists()) {
                documentFactory = DocumentBuilderFactory.newInstance();
                documentFactory.setNamespaceAware(true);
                documentBuilder = documentFactory.newDocumentBuilder();
                document = documentBuilder.parse(nessusFile);
                xpathFactory = XPathFactory.newInstance();
            } else {
                throw new IllegalArgumentException("Nessus file doesn't exist : " + nessusFile.getPath());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void parse(String filepath) throws FileNotFoundException {
        nessusFile = new File(filepath);
        if(nessusFile.exists()) {
            this.initialize();
            
        } else {
            throw new FileNotFoundException("Cannot find file : " + filepath);
        }
    }
}
