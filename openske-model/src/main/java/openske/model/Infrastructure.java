package openske.model;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Infrastructure {

    protected String name;

    protected Map<String, Map<String, InfrastructureItem>> items;
    
    public Infrastructure(String name) {
        this.name = name;
        items = new HashMap<String, Map<String, InfrastructureItem>>();
    }

    public Infrastructure(File groovyFile) {
        this(groovyFile.getName());
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
    
    public <T> T instantiate(Class<T> klass, Object... constructorParams) {
        try {
            T obj = klass.newInstance();
            Field infraField = klass.getDeclaredField("infrastructure");
            infraField.setAccessible(true);
            infraField.set(obj, this);
            infraField.setAccessible(false);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public void add(InfrastructureItem obj) {
        Class klass = obj.getClass();
        if (!items.containsKey(klass.getName())) {
            items.put(klass.getName(), new HashMap<String, InfrastructureItem>());
        }
        Map<String, InfrastructureItem> klassMap = (Map<String, InfrastructureItem>) items.get(klass.getName());
        if (!klassMap.containsKey(obj.toString())) {
            klassMap.put(obj.toString(), obj);
        }
    }

    @SuppressWarnings("unchecked")
    public InfrastructureItem get(Class klass, String identifier) {
        if (items.containsKey(klass.getName())) {
            Map<String, InfrastructureItem> klassMap = (Map<String, InfrastructureItem>) items.get(klass.getName());
            return klassMap.containsKey(identifier) ? klassMap.get(identifier) : null;
        } else {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    public Collection<InfrastructureItem> get(Class klass) {
        if (items.containsKey(klass.getName())) {
            Map<String, InfrastructureItem> klassMap = (Map<String, InfrastructureItem>) items.get(klass.getName());
            return klassMap.values();
        } else {
            return new HashSet<InfrastructureItem>();
        }
    }
    
    @SuppressWarnings("unchecked")
    public boolean has(Class klass, String identifier) {
        if( items.containsKey(klass.getName())) {
            Map<String, InfrastructureItem> klassMap = (Map<String, InfrastructureItem>) items.get(klass.getName());
            return klassMap.containsKey(identifier);
        } else {
            return false;
        }
    }
    
    public String[] types() {
        return items.keySet().toArray(new String[0]);
    }
    
    @SuppressWarnings("unchecked")
    public Collection<InfrastructureItem> list(String type) {
        if(items.containsKey(type)) {
            return ((Map<String, InfrastructureItem>)items.get(type)).values();
        } else {
            return null;
        }
    }
    
    public Collection<InfrastructureItem> list(Class klass) {
        return list(klass.getName());
    }
    
    @SuppressWarnings("unchecked")
    public String statistics()  {
        StringBuffer sb = new StringBuffer();
        try {
            sb.append("Infrastructure Statistics:\n\n");
            for(String type : items.keySet()) {
                Map<String, InfrastructureItem> typeItems = (Map<String, InfrastructureItem>)items.get(type);
                sb.append("Type : " + type + " (" + typeItems.size() + ")\n");
                for(InfrastructureItem i : typeItems.values()) {
                    sb.append("  - ");
                    sb.append(i.statistics());
                    sb.append("\n");
                }
                
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sb.toString();
    }
}
