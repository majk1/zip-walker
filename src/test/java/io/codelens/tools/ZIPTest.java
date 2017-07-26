package io.codelens.tools;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ZIPTest {

    @Test
    public void testZipInstantiation() throws Exception {
        try {
            Constructor<ZIP> constructor = ZIP.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
            fail("Instantiation should fail");
        } catch (InvocationTargetException e) {
            assertTrue(e.getCause() instanceof UnsupportedOperationException);
        }
    }

    @Test
    public void testZipUrilInstantiation() throws Exception {
        try {
            Constructor<ZIP.Util> constructor = ZIP.Util.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
            fail("Instantiation should fail");
        } catch (InvocationTargetException e) {
            assertTrue(e.getCause() instanceof UnsupportedOperationException);
        }
    }

    @Test
    public void testNullPathGetExtension() throws Exception {
        try {
            ZIP.Util.getExtension(null);
            fail("Should throw exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Path is null", e.getMessage());
        }
    }

    @Test
    public void testNullPathGetFileNameFromPath() throws Exception {
        try {
            ZIP.Util.getFileNameFromPath(null);
            fail("Should throw exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Path is null", e.getMessage());
        }
    }
}