package util;

import graph.Graph;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.*;


public class Files {

    public static void copyDirectory(String sourceDirectory, String destinationDirectory) throws IOException {
        File sourceDirectoryFile = getDirectory(sourceDirectory);
        File destinationDirectoryFile = new File(destinationDirectory);
        FileUtils.copyDirectory(sourceDirectoryFile, destinationDirectoryFile);
    }

    public static List<String> readFile(String path) throws IOException, NoSuchFieldException {
        return java.nio.file.Files.readAllLines(java.nio.file.Paths.get(path));
    }

    public static File getFile(String path) {
        return getFileOrDirectory(path, false);
    }

    private static File getDirectory(String path) {
        return getFileOrDirectory(path, true);
    }

    public static File[] getFiles(String path) {
        return Objects.requireNonNull(getDirectory(path).listFiles());
    }

    private static File getFileOrDirectory(String path, boolean isDirectory) throws IllegalArgumentException {
        File file = new File(path);
        if(!file.exists())
            throw new IllegalArgumentException(path + " " + (isDirectory ? "directory" : "file") + " does not exist.");
        if(isDirectory && !file.isDirectory() || !isDirectory && !file.isFile())
            throw new IllegalArgumentException(path + " is not a " + (isDirectory ? "directory." : "file."));
        return file;
    }

    public static Class<?> getInterfaceFromFile(File file, String name) {
        try {
            return Class.forName("specification." + name + "." +
                    file.getName().substring(0, file.getName().length() - 5));
        } catch (ClassNotFoundException e) {
            SimpleLog.printError(e, "File \"" + file.getName() + "\" is not a java class.");
        }
        return null;
    }

    public static void storeObject(String path, Graph obj) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(java.nio.file.Files.newOutputStream(Paths.get(path)))) {
            oos.writeObject(obj);
        }
    }

}
