package com.openske.drools;

import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;

public class DroolsFacade {

    protected KnowledgeBuilder knowledgeBuilder;

    public DroolsFacade() {
        knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
    }

    public void addResource(String resourceUrl) {

    }

    public void fireAllRules() {
        
    }
}
