package openske.console;

import jline.ArgumentCompletor.AbstractArgumentDelimiter;


public class HyphenArgumentDelimiter extends AbstractArgumentDelimiter {

    @Override
    public boolean isDelimiterChar(String buffer, int pos) {
        return "-".equals(buffer.charAt(pos));
    }

}
