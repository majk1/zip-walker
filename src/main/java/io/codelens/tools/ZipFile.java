package io.codelens.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

public interface ZipFile {
    
    ZipArchive getArchive();
    ZipEntry getZipEntry();
    String getFileAsString() throws IOException;
    String[] getFileAsStringArray() throws IOException;
    byte[] getFile() throws IOException;
    InputStream getInputStream();

}
