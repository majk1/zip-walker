package io.codelens.tools;

import java.io.IOException;

@FunctionalInterface
public interface ZipWalker {
    
    void onItem(ZipFile file) throws IOException;

}
