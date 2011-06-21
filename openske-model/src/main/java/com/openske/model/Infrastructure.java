package com.openske.model;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Infrastructure {

    protected String name;

    protected Map<String, Object> items = new HashMap<String, Object>();

    public Infrastructure(File groovyFile) {
        this.name = groovyFile.getName();
        evaluateGroovyCode(groovyFile);
    }

    private void evaluateGroovyCode(File groovyFile) {
        try {
            Binding params = new Binding();
            params.setVariable("infrastructure", this);
            params.setVariable("infra", this);
            GroovyShell shell = new GroovyShell(params);
            shell.evaluate(groovyFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void add(Object obj) {
        Class klass = obj.getClass();
        if (!items.containsKey(klass.getName())) {
            items.put(klass.getName(), new HashMap<String, Object>());
        }
        Map<String, Object> klassMap = (Map<String, Object>) items.get(klass.getName());
        if (!klassMap.containsKey(obj.toString())) {
            klassMap.put(obj.toString(), obj);
        }
    }

    @SuppressWarnings("unchecked")
    public Object get(Class klass, String identifier) {
        if (items.containsKey(klass.getName())) {
            Map<String, Object> klassMap = (Map<String, Object>) items.get(klass.getName());
            return klassMap.containsKey(identifier) ? klassMap.get(identifier) : null;
        } else {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    public boolean has(Class klass, String identifier) {
        if( items.containsKey(klass.getName())) {
            Map<String, Object> klassMap = (Map<String, Object>) items.get(klass.getName());
            return klassMap.containsKey(identifier);
        } else {
            return false;
        }
    }
    
    public String[] types() {
        return items.keySet().toArray(new String[0]);
    }
    
    @SuppressWarnings("unchecked")
    public Collection<Object> list(String type) {
        if(items.containsKey(type)) {
            return ((Map<String, Object>)items.get(type)).values();
        } else {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    public String statistics() {
        StringBuffer sb = new StringBuffer();
        sb.append("Infrastructure Statistics:\n\n");
        for(String type : items.keySet()) {
            sb.append("Type : " + type + " ==> " + ((Map<String, Object>)items.get(type)).size() + "\n");
        }
        return sb.toString();
    }
}
