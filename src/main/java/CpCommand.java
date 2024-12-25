package ru.spbstu.telematics.java;

import java.io.*;
import java.nio.file.*;

public class CpCommand {

    public static void copy(String sourcePath, String destinationPath) throws IOException {
        Path source = Paths.get(sourcePath);
        Path destination = Paths.get(destinationPath);

        if (!Files.exists(source)) {
            throw new FileNotFoundException("Source file or directory does not exist: " + sourcePath);
        }

        if (Files.isDirectory(source)) {
            copyDirectory(source, destination);
        } else {
            copyFile(source, destination);
        }
    }

    private static void copyFile(Path source, Path destination) throws IOException {
        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
    }

    private static void copyDirectory(Path sourceDir, Path destDir) throws IOException {
        Files.walk(sourceDir).forEach(sourcePath -> {
            try {
                Path targetPath = destDir.resolve(sourceDir.relativize(sourcePath));
                if (Files.isDirectory(sourcePath)) {
                    Files.createDirectories(targetPath);
                } else {
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                throw new RuntimeException("Error copying directory: " + e.getMessage(), e);
            }
        });
    }
}
