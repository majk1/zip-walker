package io.codelens.tools.internal;

import io.codelens.tools.ZIP;
import io.codelens.tools.ZipArchive;
import io.codelens.tools.ZipWalker;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZipArchiveImpl implements ZipArchive {
    
    private static final char SEPARATOR = '/';
    private static final Set<String> ZIP_EXTENSIONS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("zip", "jar", "war", "ear")));
    
    private String name;
    private String extension;
    private String baseName;
    private String path;
    private String absolutePath;
    private ZipArchiveImpl parent;
    
    @Override
    public boolean hasParent() {
        return parent != null;
    }
    
    @Override
    public void walk(ZipWalker zipWalker) throws IOException {
        walk(ZIP_EXTENSIONS, null, zipWalker);
    }

    @Override
    public void walk(Set<String> zipExtensions, ZipWalker zipWalker) throws IOException {
        walk(zipExtensions, null, zipWalker);
    }

    private boolean isArchive(Set<String> zipExtensions, ZipEntry entry) {
        return zipExtensions != null && zipExtensions.contains(ZIP.Util.getExtension(entry.getName()));
    }

    @SuppressWarnings("squid:S2095")
    private void walk(Set<String> zipExtensions, InputStream inputStream, ZipWalker zipWalker) throws IOException {
        final boolean closeStream;
        final ZipInputStream zipInputStream;
        if (inputStream != null) {
            zipInputStream = new ZipInputStream(inputStream);
            closeStream = false;
        } else {
            zipInputStream = new ZipInputStream(new FileInputStream(absolutePath));
            closeStream = true;
        }
        
        ZipEntry entry;
        while ((entry = zipInputStream.getNextEntry()) != null) {
            if (isArchive(zipExtensions, entry)) {
                ZipArchiveImpl.withParent(entry.getName(), this).walk(zipExtensions, zipInputStream, zipWalker);
            } else {
                zipWalker.onItem(ZipFileImpl.create(this, entry, zipInputStream));
            }
        }
        
        if (closeStream) {
            zipInputStream.close();
        }
    }

    private String getPathWithFilename() {
        return path.isEmpty() ? name : path + SEPARATOR + name;
    }

    public static ZipArchiveImpl of(String path) {
        return withParent(path, null);
    }

    protected static ZipArchiveImpl withParent(String path, ZipArchiveImpl parent) {
        ZipArchiveImpl zipArchive = new ZipArchiveImpl();
        
        int lastIndexOfSeparator = path.lastIndexOf(SEPARATOR);
        if (lastIndexOfSeparator == -1) {
            zipArchive.setPath("");
            zipArchive.setName(path.trim());
        } else {
            zipArchive.setPath(path.substring(0, lastIndexOfSeparator).trim());
            zipArchive.setName(path.substring(lastIndexOfSeparator + 1).trim());
        }

        zipArchive.setParent(parent);
        if (zipArchive.hasParent()) {
            String parentAbsolutePath = zipArchive.getParent().getAbsolutePath();
            zipArchive.setAbsolutePath(parentAbsolutePath + "!" + SEPARATOR + zipArchive.getPathWithFilename());
        } else {
            zipArchive.setAbsolutePath(zipArchive.getPathWithFilename());
        }

        int lastIndexOfDot = zipArchive.getName().lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            zipArchive.setExtension("");
            zipArchive.setBaseName(zipArchive.getName());
        } else {
            zipArchive.setExtension(zipArchive.getName().substring(lastIndexOfDot + 1));
            zipArchive.setBaseName(zipArchive.getName().substring(0, lastIndexOfDot));
        }
        
        return zipArchive;
    }

}
