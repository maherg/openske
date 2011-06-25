package openske.nessus;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import openske.model.Infrastructure;
import openske.model.hardware.Connectable;
import openske.model.hardware.Host;
import openske.model.measurablesecurity.CpeEntry;
import openske.model.software.Software;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class NessusFileParser {

    private static final String NESSUS_REPORT_HOST_NODE = "ReportHost";
    private static final String NESSUS_REPORT_ITEM_NODE = "ReportItem";
    private static final String NESSUS_CPE_ENTRY_NODE = "cpe";
    private static final String NESSUS_CVE_ENTRY_NODE = "cve";

    private File nessusFile;
    private Infrastructure infrastructure;
    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private Document document;

    public NessusFileParser(File nessusFile, Infrastructure infrastructure) {
        this.nessusFile = nessusFile;
        this.infrastructure = infrastructure;
    }

    private void initialize() throws Exception {
        if (nessusFile.exists()) {
            factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            builder = factory.newDocumentBuilder();
            document = builder.parse(nessusFile);
            document.getDocumentElement().normalize();
        } else {
            throw new FileNotFoundException("Nessus file doesn't exist : " + nessusFile.getPath());
        }
    }

    public void parse() throws Exception {
        initialize();

        // Loop over the host nodes
        NodeList hostNodeList = document.getElementsByTagName(NESSUS_REPORT_HOST_NODE);
        for (int i = 0; i < hostNodeList.getLength(); i++) {

            Element hostNode = (Element) hostNodeList.item(i);
            if (hostNode.getNodeType() == Node.ELEMENT_NODE) {
                Host host = extractHost(hostNode);

                // Loop over the vulnerability nodes
                NodeList vulnNodeList = hostNode.getElementsByTagName(NESSUS_REPORT_ITEM_NODE);
                for (int j = 0; j < vulnNodeList.getLength(); j++) {
                    Element vulnNode = (Element) vulnNodeList.item(j);

                    if (hasCpeEntry(vulnNode)) {
                        String cpeId = extractCpeId(vulnNode);
                        Software software = new Software(cpeId, host);
                        
                        if(infrastructure.has(Software.class, cpeId)) {
                            software = (Software) infrastructure.get(Software.class, cpeId);
                        }
                        
                        host.addSoftware(software);
                        infrastructure.add(software);

                        software.addVulnerabilities(extractCveIds(vulnNode).toArray(new String[0]));
                        
                    }
                }
            }
        }
    }

    private Host extractHost(Element hostNode) {        
        String hostAddress = hostNode.getAttribute("name");
        // FIXME : Assuming all NEW findings are hosts
        if (infrastructure.has(Host.class, hostAddress)) {
            return (Host) infrastructure.get(Connectable.class, hostAddress);
        } else {
            Host host = new Host(hostAddress);
            infrastructure.add(host);
            return host;
        }
    }

    private boolean hasCpeEntry(Element vulnNode) {
        NodeList vulnContentsList = vulnNode.getElementsByTagName(NESSUS_CPE_ENTRY_NODE);
        return vulnContentsList.getLength() > 0;
    }

    private String extractCpeId(Element vulnNode) {
        NodeList vulnContents = vulnNode.getElementsByTagName(NESSUS_CPE_ENTRY_NODE);
        Element cpeNode = (Element) vulnContents.item(0);
        return cpeNode.getTextContent();
    }

    private List<String> extractCveIds(Element vulnNode) {
        List<String> cveIds = new ArrayList<String>();

        NodeList vulnContents = vulnNode.getElementsByTagName(NESSUS_CVE_ENTRY_NODE);
        for (int i = 0; i < vulnContents.getLength(); i++) {
            Node node = vulnContents.item(i);
            cveIds.add(node.getTextContent());
        }

        return cveIds;
    }
}
