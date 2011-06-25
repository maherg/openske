package openske.model.software;

import java.util.ArrayList;
import java.util.List;

import openske.model.datasets.Dataset;
import openske.model.datasets.DatasetQuery;
import openske.model.measurablesecurity.CweEntry;

import org.w3c.dom.Node;


public class Weakness implements CweEntry, Comparable<Weakness> {
    
    public static final String CWE_WEAKNESS_NODE = "Weakness";
    public static final String CWE_WEAKNESS_ATTRIBUTE_IDENTIFIER = "ID";
    public static final String CWE_WEAKNESS_ATTRIBUTE_NAME = "Name";

    protected String identifier;
    protected String name;
    protected Vulnerability vulnerability;
    protected Software software;
    protected static DatasetQuery dataset = new DatasetQuery(Dataset.CWE);

    public static List<Weakness> forVulnerability(Vulnerability vuln) {
        List<Weakness> weaknesses = new ArrayList<Weakness>();
        List<Node> cweNodes = dataset.query(CWE_WEAKNESS_NODE, vuln.cveId());
        for(Node cweNode : cweNodes) {
            String cweId = cweNode.getAttributes().getNamedItem(CWE_WEAKNESS_ATTRIBUTE_IDENTIFIER).getNodeValue();
            String cweName = cweNode.getAttributes().getNamedItem(CWE_WEAKNESS_ATTRIBUTE_NAME).getNodeValue();
            Weakness wk = new Weakness("CWE-" + cweId, vuln);
            wk.setName(cweName);
            weaknesses.add(wk);
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
    
    @Override
    public String toString() {
        return cweId();
    }

    @Override
    public String cweId() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Weakness w) {
        return identifier.compareToIgnoreCase(w.getIdentifier());
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Weakness && ((Weakness)obj).cweId().equals(identifier);
    }
}
