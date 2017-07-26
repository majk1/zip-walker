package io.codelens.tools.internal;

import io.codelens.tools.ZIP;
import io.codelens.tools.ZipArchive;
import io.codelens.tools.ZipFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

public class ZipFileImpl implements ZipFile {

    private ZipArchive archive;
    private ZipEntry entry;
    private InputStream inputStream;

    private ZipFileImpl(ZipArchive archive, ZipEntry entry, InputStream inputStream) {
        this.archive = archive;
        this.entry = entry;
        this.inputStream = inputStream;
    }

    @Override
    public ZipArchive getArchive() {
        return archive;
    }

    @Override
    public ZipEntry getZipEntry() {
        return entry;
    }

    @Override
    public String getFileAsString() throws IOException {
        return ZIP.Util.readTextFile(inputStream);
    }

    @Override
    public String[] getFileAsStringArray() throws IOException {
        return ZIP.Util.readTextFileLines(inputStream);
    }

    @Override
    public byte[] getFile() throws IOException {
        return ZIP.Util.readFile(inputStream);
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    protected static ZipFile create(ZipArchive archive, ZipEntry entry, InputStream inputStream) {
        return new ZipFileImpl(archive, entry, inputStream);
    }

}
