package com.openske.model.datasets;

import java.util.List;

public class CWE {
    
    public static List<String> findByVulnerability(String cveId) {
        try {
            DatasetQuery dquery = new DatasetQuery(Dataset.CWE);
            List<String> cweIds = dquery.query("Weakness", cveId);
            return cweIds;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
