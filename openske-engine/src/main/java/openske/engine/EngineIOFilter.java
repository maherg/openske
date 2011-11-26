package openske.engine;

import java.io.File;

import org.apache.commons.io.filefilter.AbstractFileFilter;

public class EngineIOFilter extends AbstractFileFilter {

    /**
     * Skips Maven's target directory contents
     */
    @Override
    public boolean accept(File dir, String name) {
        return ! dir.getAbsolutePath().contains("/target/");
    }

}
