package com.openske.model.datasets;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class DatasetQuery {

    protected Dataset dataset;

    public DatasetQuery(Dataset dataset) {
        this.dataset = dataset;
    }

    public List<String> query(String nodeName, String searchText) throws Exception {
        List<String> results = new ArrayList<String>();
        URL url = dataset.asURL();
        InputStream in = url.openStream();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = factory.createXMLEventReader(in);
        XMLEventReader filteredReader = factory.createFilteredReader(eventReader, new EventFilter() {
            public boolean accept(XMLEvent event) {
                return event.isStartElement();
            }
        });
        while (filteredReader.hasNext()) {
            XMLEvent event = filteredReader.nextEvent();
            StartElement element = event.asStartElement();
            if (element.getName().toString().equals(nodeName) && filteredReader.getElementText().contains(searchText)) {
                String idValue = element.getAttributeByName(new QName("ID")).getValue();
                results.add(idValue);
            }
        }
        
        filteredReader.close();
        eventReader.close();

        return results;
    }
}
