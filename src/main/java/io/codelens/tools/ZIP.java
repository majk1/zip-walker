package io.codelens.tools;

import io.codelens.tools.internal.ZipArchiveImpl;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class ZIP {
    
    private ZIP() {
        throw new UnsupportedOperationException();
    }
    
    public static ZipArchive of(String path) {
        return ZipArchiveImpl.of(path);
    }
    
    public static final class Util {

        private static final int DEFAULT_READ_BUFFER_SIZE = 4096;
        
        private Util() {
            throw new UnsupportedOperationException();
        }

        public static String readTextFile(InputStream inputStream) throws IOException {
            return readTextFile(inputStream, DEFAULT_READ_BUFFER_SIZE);
        }

        public static String readTextFile(InputStream inputStream, int readBufferSize) throws IOException {
            byte[] buffer = new byte[readBufferSize];
            StringBuilder sb = new StringBuilder();

            int readBytes;
            while ((readBytes = inputStream.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, readBytes));
            }

            return sb.toString();
        }

        public static byte[] readFile(InputStream inputStream) throws IOException {
            return readFile(inputStream, DEFAULT_READ_BUFFER_SIZE);
        }

        public static byte[] readFile(InputStream inputStream, int readBufferSize) throws IOException {
            byte[] buffer = new byte[readBufferSize];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            int readBytes;
            while ((readBytes = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, readBytes);
            }

            return bos.toByteArray();
        }

        public static String[] readTextFileLines(InputStream inputStream) throws IOException {
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            return lines.toArray(new String[lines.size()]);
        }

        public static String getExtension(String path) {
            if (path != null) {
                String fileName = getFileNameFromPath(path);
                int lastIndexOfDot = fileName.lastIndexOf('.');
                if (lastIndexOfDot != -1) {
                    return fileName.substring(lastIndexOfDot + 1);
                } else {
                    return "";
                }
            }
            throw new IllegalArgumentException("Path is null");
        }

        public static String getFileNameFromPath(String path) {
            if (path != null) {
                int lastIndexOfSeparator = path.lastIndexOf(File.separatorChar);
                if (lastIndexOfSeparator != -1) {
                    return path.substring(lastIndexOfSeparator + 1);
                } else {
                    return path.trim();
                }
            }
            throw new IllegalArgumentException("Path is null");
        }
        
    }

}
