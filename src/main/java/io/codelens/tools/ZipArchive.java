package io.codelens.tools;

import java.io.IOException;
import java.util.Set;

public interface ZipArchive {

    String getName();
    String getExtension();
    String getBaseName();
    String getPath();
    String getAbsolutePath();
    boolean hasParent();
    ZipArchive getParent();

    void walk(ZipWalker zipWalker) throws IOException;
    void walk(Set<String> zipExtensions, ZipWalker zipWalker) throws IOException;

}
