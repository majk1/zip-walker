package io.codelens.tools.internal;

import io.codelens.tools.ZipArchive;
import io.codelens.tools.ZipFile;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

public class ZipFileImplTest {
    
    private static final String TEST_CONTENT_STRING = "First line\nSecond line\nThird line";
    private static final byte[] TEST_CONTENT_BYTES = TEST_CONTENT_STRING.getBytes();
    private static final ZipArchive TEST_ARCHIVE = mockArchive();

    @Test
    public void testZipEntry() throws Exception {
        ZipFile zipFile = ZipFileImpl.create(null, null, null);
        assertNull(zipFile.getZipEntry());
    }

    @Test
    public void testArchive() throws Exception {
        ZipFile zipFile = ZipFileImpl.create(TEST_ARCHIVE, null, null);
        assertSame(TEST_ARCHIVE, zipFile.getArchive());
    }

    @Test
    public void testInputStream() throws Exception {
        InputStream is = createInputStream();
        ZipFile zipFile = ZipFileImpl.create(null, null, is);
        assertSame(is, zipFile.getInputStream());
    }

    @Test
    public void testReadFile() throws Exception {
        ZipFile zipFile = ZipFileImpl.create(TEST_ARCHIVE, null, createInputStream());
        byte[] fileBytes = zipFile.getFile();
        assertArrayEquals(TEST_CONTENT_BYTES, fileBytes);
    }

    @Test
    public void testReadFileString() throws Exception {
        ZipFile zipFile = ZipFileImpl.create(TEST_ARCHIVE, null, createInputStream());
        String content = zipFile.getFileAsString();
        assertEquals(TEST_CONTENT_STRING, content);
    }

    @Test
    public void testReadFileStringLines() throws Exception {
        ZipFile zipFile = ZipFileImpl.create(TEST_ARCHIVE, null, createInputStream());
        String[] lines = zipFile.getFileAsStringArray();
        assertNotNull(lines);
        assertEquals(3, lines.length);
        assertEquals("First line", lines[0]);
        assertEquals("Second line", lines[1]);
        assertEquals("Third line", lines[2]);
    }
    
    private static InputStream createInputStream() {
        return new ByteArrayInputStream(TEST_CONTENT_BYTES);
    }
    
    private static ZipArchive mockArchive() {
        return ZipArchiveImpl.of("/some/path/archive.zip");
    }
        
}