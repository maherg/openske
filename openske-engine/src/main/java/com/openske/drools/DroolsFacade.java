package com.openske.drools;

import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;

public class DroolsFacade {

    protected KnowledgeBuilder kBuilder;

    public DroolsFacade() {
        kBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
    }

    public void addResource(String resourceUrl) {

    }

    public void fireAllRules() {

    }
}
