package com.openske.drools;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.drools.builder.ResourceType;

import com.openske.engine.Engine;
import com.openske.engine.EngineIOFilter;

public class DroolsResourceHelper {
    
    @SuppressWarnings("unchecked")
    public static Collection<File> listResources(ResourceType type) {
        String suffix = null;
        if(ResourceType.DRL.equals(type)) {
            suffix = "drl";
        } else if(ResourceType.DRF.equals(type)) {
            suffix = "rf";
        }
        return FileUtils.listFiles(Engine.currentWorkingDirectory(), FileFilterUtils.suffixFileFilter(suffix), new EngineIOFilter());
    }
    
    @SuppressWarnings("unchecked")
    public static Collection<File> listResources(String[] extensions) {
        if(extensions == null || extensions.length == 0) {
            return new ArrayList<File>();
        }
        return FileUtils.listFiles(Engine.currentWorkingDirectory(), extensions , true);
    }
}
