package openske.drools;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class DroolsRuleFlowHelper {

    /**
     * Extracts the Rule Flow ID from it's file. This is used to start the rule
     * flow
     * 
     * @param file
     * @return
     * @throws Exception
     */
    public static String extractRuleFlowId(File file) {
        try {
            // Prepare the document to be parsed
            DocumentBuilderFactory domFactory = DocumentBuilderFactory
                    .newInstance();
            domFactory.setNamespaceAware(true);
            DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
            Document doc = domBuilder.parse(file);
            
            return doc.getDocumentElement().getAttribute("id");

        } catch (Exception e) {
            // This shouldn't happen
            e.printStackTrace();
        }
        return null;
    }
}
