package openske.model.datasets;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DatasetQuery {

    protected Dataset dataset;
    protected DocumentBuilderFactory factory;
    protected DocumentBuilder builder;
    protected Document document;

    public DatasetQuery(Dataset dataset) {
        this.dataset = dataset;
        factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
            builder = factory.newDocumentBuilder();
            URL url = dataset.asURL();
            InputStream in = url.openStream();
            document = builder.parse(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Node> query(String nodeName, String searchText) {
        List<Node> results = new ArrayList<Node>();
        
        try {
            NodeList nodeList = document.getElementsByTagName(nodeName);
            for(int i = 0;i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if(node.getTextContent().contains(searchText)) {
                    results.add(node);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return results;
    }
}
