package openske.drools;

import java.io.File;
import java.util.Collection;

import openske.engine.Engine;
import openske.engine.EngineIOFilter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;


public class DroolsResourceHelper {
    
    @SuppressWarnings("unchecked")
    public static Collection<File> listResources(String extension) {
        return FileUtils.listFiles(Engine.currentWorkingDirectory(), FileFilterUtils.suffixFileFilter(extension), new EngineIOFilter());
    }
}
