package querylang;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;
    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }
    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }
    @Test
    void testErrorEmptyCommand() {
        System.setIn(new ByteArrayInputStream("\n".getBytes()));
        Main.main(new String[]{});
        assertEquals("ERROR: Oops, empty command!", outContent.toString().trim());
    }
    @Test
    void testMessageWithoutErrorsInsertDefaultPeople() {
        System.setIn(new ByteArrayInputStream("INSERT (Кто, то, Прага, 90)\n".getBytes()));
        Main.main(new String[]{});
        assertTrue(outContent.toString().trim().matches("User with id \\d+ was added successfully"));
    }
}
