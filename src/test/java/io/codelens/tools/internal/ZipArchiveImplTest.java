package io.codelens.tools.internal;

import io.codelens.tools.ZIP;
import io.codelens.tools.ZipArchive;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class ZipArchiveImplTest {

    @Test
    public void testSimplePathlessFile() throws Exception {
        ZipArchiveImpl path = ZipArchiveImpl.of("some.archive.ear");

        assertEquals("some.archive.ear", path.getName());
        assertEquals("some.archive", path.getBaseName());
        assertEquals("ear", path.getExtension());
        assertEquals("", path.getPath());
        assertEquals("some.archive.ear", path.getAbsolutePath());
        assertNull(path.getParent());
        assertFalse(path.hasParent());
    }

    @Test
    public void testSimpleFileWithPath() throws Exception {
        ZipArchiveImpl path = ZipArchiveImpl.of("only-one.dir/some.archive.ear");

        assertEquals("some.archive.ear", path.getName());
        assertEquals("some.archive", path.getBaseName());
        assertEquals("ear", path.getExtension());
        assertEquals("only-one.dir", path.getPath());
        assertEquals("only-one.dir/some.archive.ear", path.getAbsolutePath());
        assertNull(path.getParent());
        assertFalse(path.hasParent());
    }

    @Test
    public void testExtensionLessFileWithPath() throws Exception {
        ZipArchiveImpl path = ZipArchiveImpl.of("only-one.dir/some_archive");

        assertEquals("some_archive", path.getName());
        assertEquals("some_archive", path.getBaseName());
        assertEquals("", path.getExtension());
        assertEquals("only-one.dir", path.getPath());
        assertEquals("only-one.dir/some_archive", path.getAbsolutePath());
        assertNull(path.getParent());
        assertFalse(path.hasParent());
    }

    @Test
    public void testSimpleFileWithParentPath() throws Exception {
        ZipArchiveImpl parent = ZipArchiveImpl.of("only-one.dir/some.archive.ear");
        ZipArchiveImpl path = ZipArchiveImpl.withParent("lib/some-frontend.war", parent);

        assertEquals("some-frontend.war", path.getName());
        assertEquals("some-frontend", path.getBaseName());
        assertEquals("war", path.getExtension());
        assertEquals("lib", path.getPath());
        assertEquals("only-one.dir/some.archive.ear!/lib/some-frontend.war", path.getAbsolutePath());
        assertNotNull(path.getParent());
        assertTrue(path.hasParent());
    }

    @Test
    public void testSimpleFileWithMoreParentPath() throws Exception {
        ZipArchiveImpl earPath = ZipArchiveImpl.of("/opt/server/deployment-directory/archive.ear");
        ZipArchiveImpl warPath = ZipArchiveImpl.withParent("frontend.war", earPath);
        ZipArchiveImpl path = ZipArchiveImpl.withParent("WEB-INF/lib/some-api.jar", warPath);

        assertEquals("some-api.jar", path.getName());
        assertEquals("some-api", path.getBaseName());
        assertEquals("jar", path.getExtension());
        assertEquals("WEB-INF/lib", path.getPath());
        assertEquals("/opt/server/deployment-directory/archive.ear!/frontend.war!/WEB-INF/lib/some-api.jar", path.getAbsolutePath());
        assertNotNull(path.getParent());
        assertTrue(path.hasParent());
    }

    @Test
    public void testSimpleFileWithMoreParentPath2() throws Exception {
        ZipArchiveImpl earPath = ZipArchiveImpl.of("archive.ear");
        ZipArchiveImpl warPath = ZipArchiveImpl.withParent("frontend.war", earPath);
        ZipArchiveImpl path = ZipArchiveImpl.withParent("WEB-INF/lib/some-api.jar", warPath);

        assertEquals("some-api.jar", path.getName());
        assertEquals("some-api", path.getBaseName());
        assertEquals("jar", path.getExtension());
        assertEquals("WEB-INF/lib", path.getPath());
        assertEquals("archive.ear!/frontend.war!/WEB-INF/lib/some-api.jar", path.getAbsolutePath());
        assertNotNull(path.getParent());
        assertTrue(path.hasParent());
    }

    @Test
    public void testStrictPath() throws Exception {
        ZipArchiveImpl earPath = ZipArchiveImpl.of("archive.ear");
        ZipArchiveImpl warPath = ZipArchiveImpl.withParent("frontend.war", earPath);
        ZipArchiveImpl path = ZipArchiveImpl.withParent("some-api.jar", warPath);

        assertEquals("some-api.jar", path.getName());
        assertEquals("some-api", path.getBaseName());
        assertEquals("jar", path.getExtension());
        assertEquals("", path.getPath());
        assertEquals("archive.ear!/frontend.war!/some-api.jar", path.getAbsolutePath());
        assertNotNull(path.getParent());
        assertTrue(path.hasParent());
    }

    @Test
    public void testWalker() throws Exception {
        Set<String> checkResults = new HashSet<>();
        
        ZipArchive archive = ZIP.of("src/test/resources/test.zip");
        archive.walk(file -> {
            if ("test.zip".equals(file.getArchive().getName())) {
                checkResults.add("testZipArchiveNameCheck");
            }
            if ("inner.zip".equals(file.getArchive().getName()) && file.getArchive().hasParent() && file.getArchive().getParent().getName().equals("test.zip")) {
                checkResults.add("innerZipArchiveCheck");
            }
        });
        
        assertTrue(checkResults.contains("testZipArchiveNameCheck"));
        assertTrue(checkResults.contains("innerZipArchiveCheck"));
    }

    @Test
    public void testWalkerWithArchiveExtensions() throws Exception {
        Set<String> zipExtensions = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("jar")));
        Set<String> checkResults = new HashSet<>();

        ZipArchive archive = ZIP.of("src/test/resources/test.zip");
        archive.walk(zipExtensions, file -> {
            if ("test.zip".equals(file.getArchive().getName())) {
                checkResults.add("testZipArchiveNameCheck");
            }
            if ("inner.zip".equals(file.getArchive().getName()) && file.getArchive().hasParent() && file.getArchive().getParent().getName().equals("test.zip")) {
                checkResults.add("innerZipArchiveCheck");
            }
        });

        assertTrue(checkResults.contains("testZipArchiveNameCheck"));
        assertFalse(checkResults.contains("innerZipArchiveCheck"));
    }

    @Test
    public void testWalkerWithNullArchiveExtensions() throws Exception {
        Set<String> checkResults = new HashSet<>();

        ZipArchive archive = ZIP.of("src/test/resources/test.zip");
        archive.walk(null, file -> {
            if ("test.zip".equals(file.getArchive().getName())) {
                checkResults.add("testZipArchiveNameCheck");
            }
            if ("inner.zip".equals(file.getArchive().getName()) && file.getArchive().hasParent() && file.getArchive().getParent().getName().equals("test.zip")) {
                checkResults.add("innerZipArchiveCheck");
            }
        });

        assertTrue(checkResults.contains("testZipArchiveNameCheck"));
        assertFalse(checkResults.contains("innerZipArchiveCheck"));
    }
}
