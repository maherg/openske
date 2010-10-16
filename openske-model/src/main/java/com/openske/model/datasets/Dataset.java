package com.openske.model.datasets;

import java.net.URL;

public enum Dataset {
    CVE,
    CWE,
    CPE,
    CCE,
    CAPEC,
    ;
    
    public URL asURL() {
        return Thread.currentThread().getContextClassLoader().getResource(this.name() + ".xml");
    }
}
