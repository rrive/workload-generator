package util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static util.Time.sleep;

public class Files {

    public static void copyDirectory(String sourceDirectory, String destinationDirectory) throws IOException, InterruptedException {
        File sourceDirectoryFile = getDirectory(sourceDirectory);
        File destinationDirectoryFile = new File(destinationDirectory);
        FileUtils.copyDirectory(sourceDirectoryFile, destinationDirectoryFile);
        sleep(1);
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

}
