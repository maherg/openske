package openske.model.software;

import java.util.ArrayList;
import java.util.List;

import openske.model.datasets.Dataset;
import openske.model.datasets.DatasetQuery;

import org.w3c.dom.Node;


public class Weakness {
    
    public static final String CWE_WEAKNESS_NODE = "Weakness";
    public static final String CWE_WEAKNESS_NODE_IDENTIFIER = "ID";

    protected String identifier;
    protected Vulnerability vulnerability;
    protected Software software;
    protected static DatasetQuery dataset = new DatasetQuery(Dataset.CWE);

    public static List<Weakness> forVulnerability(Vulnerability vuln) {
        List<Weakness> weaknesses = new ArrayList<Weakness>();
        List<Node> cweNodes = dataset.query(CWE_WEAKNESS_NODE, vuln.cveId());
        for(Node cweNode : cweNodes) {
            String cweId = cweNode.getAttributes().getNamedItem(CWE_WEAKNESS_NODE_IDENTIFIER).getNodeValue();
            weaknesses.add(new Weakness(cweId, vuln));
        }
        return weaknesses;
    }
    
    public Weakness(String identifier, Vulnerability vuln) {
        this.identifier = identifier;
        this.vulnerability = vuln;
        this.software = vuln.getSoftware();
        this.software.addWeakness(this);
    }
    
    public Weakness(String identifier, Software software) {
        this.identifier = identifier;
        this.software = software;
        this.software.addWeakness(this);
        this.vulnerability = null;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Vulnerability getVulnerability() {
        return vulnerability;
    }

    public void setVulnerability(Vulnerability vulnerability) {
        this.vulnerability = vulnerability;
    }

    public Software getSoftware() {
        return software;
    }

    public void setSoftware(Software software) {
        this.software = software;
    }
}
