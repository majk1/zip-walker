package io.codelens.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class TestApp {

    public static void main(String[] args) throws IOException {

        Set<String> zipExts = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("war")));
        String zipFilePath = "/Users/majki/Developer/Project/ulyssys-svn/iier2/trunk/iier2-ear/target/iier2-ear.ear";

        ZipArchive archive = ZIP.of(zipFilePath);
        archive.walk(zipExts, file -> {
            // System.out.println(file.getArchive().getAbsolutePath() + " -> " + file.getZipEntry().getName());
            if (file.getZipEntry().getName().endsWith("pom.properties")) {
                System.out.println(file.getArchive().getAbsolutePath() + " -> " + file.getZipEntry().getName());
                printArchiveTree(file.getArchive());
                System.out.println("### " + gavFromInputStreamProperties(file.getInputStream()));
                System.out.println();
            }
        });
    }

    private static void printArchiveTree(ZipArchive archive) {
        List<String> archiveNames = new ArrayList<>();
        ZipArchive currentArchive = archive;
        while (currentArchive != null) {
            archiveNames.add(currentArchive.getName());
            currentArchive = currentArchive.getParent();
        }
        Collections.reverse(archiveNames);
        int level = 0;
        for (String archiveName : archiveNames) {
            if (level > 0) {
                for (int i = 0; i < level * 4; i++) {
                    System.out.print(" ");
                }
                System.out.print("'- ");
            }
            System.out.println(archiveName);
            level++;
        }
    }

    private static String gavFromInputStreamProperties(InputStream inputStream) throws IOException {
        Properties properties = new Properties();
        properties.load(inputStream);
        return properties.getProperty("groupId") + ":" + properties.getProperty("artifactId") + ":" + properties.getProperty("version");
    }

}
