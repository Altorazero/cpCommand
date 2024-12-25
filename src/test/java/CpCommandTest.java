import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.spbstu.telematics.java.CpCommand;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class CpCommandTest {

    private static final String TEST_DIR = "test_dir";
    private static final String SOURCE_FILE = TEST_DIR + "/source.txt";
    private static final String DEST_FILE = TEST_DIR + "/dest.txt";
    private static final String SOURCE_DIR = TEST_DIR + "/sourceDir";
    private static final String DEST_DIR = TEST_DIR + "/destDir";

    @Before
    public void setup() throws IOException {
        // Create test directory and files
        Files.createDirectories(Paths.get(TEST_DIR));
        Files.writeString(Paths.get(SOURCE_FILE), "This is a test file.");
        Files.createDirectories(Paths.get(SOURCE_DIR));
        Files.writeString(Paths.get(SOURCE_DIR + "/file1.txt"), "File in directory");
    }

    @After
    public void cleanup() throws IOException {
        // Delete test directory
        Files.walk(Paths.get(TEST_DIR))
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    public void testCopyFile() throws IOException {
        CpCommand.copy(SOURCE_FILE, DEST_FILE);
        assertTrue("Destination file should exist", Files.exists(Paths.get(DEST_FILE)));
        assertEquals("Content should match", "This is a test file.", Files.readString(Paths.get(DEST_FILE)));
    }

    @Test
    public void testCopyDirectory() throws IOException {
        CpCommand.copy(SOURCE_DIR, DEST_DIR);
        assertTrue("Destination directory should exist", Files.exists(Paths.get(DEST_DIR)));
        assertTrue("File inside directory should exist", Files.exists(Paths.get(DEST_DIR + "/file1.txt")));
        assertEquals("Content should match", "File in directory", Files.readString(Paths.get(DEST_DIR + "/file1.txt")));
    }

    @Test(expected = IOException.class)
    public void testSourceDoesNotExist() throws IOException {
        CpCommand.copy("nonexistent.txt", DEST_FILE);
    }
}
