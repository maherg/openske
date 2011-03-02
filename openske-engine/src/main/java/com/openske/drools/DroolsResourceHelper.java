package com.openske.drools;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import com.openske.engine.Engine;
import com.openske.engine.EngineIOFilter;

public class DroolsResourceHelper {
    
    @SuppressWarnings("unchecked")
    public static Collection<File> listResources(String extension) {
        return FileUtils.listFiles(Engine.currentWorkingDirectory(), FileFilterUtils.suffixFileFilter(extension), new EngineIOFilter());
    }
}
