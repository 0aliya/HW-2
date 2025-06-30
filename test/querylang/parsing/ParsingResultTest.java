package querylang.parsing;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParsingResultTest {
    @Test
    void testMethodOfErrorResult() {
        ParsingResult<Object> result = ParsingResult.error("Oh!Error!");
        assertTrue(result.isError());
        assertFalse(result.isPresent());
        assertEquals("Oh!Error!", result.getErrorMessage());
    }
    @Test
    void testMethodOfRightResult() {
        ParsingResult<String> result = ParsingResult.of("Everything is fine :)");
        assertTrue(result.isPresent());
        assertFalse(result.isError());
        assertEquals("Everything is fine :)", result.getValue());
    }
    @Test
    void testMethodGetValueWithOnErrorResult() {
        ParsingResult<String> result = ParsingResult.error("error!");
        assertThrows(NullPointerException.class, result::getValue);
    }
    @Test
    void testMethodGetErrorMessageWithOneRightResult() {
        ParsingResult<String> result = ParsingResult.of("Everything is fine :)");
        assertThrows(NullPointerException.class, result::getErrorMessage);
    }
    @Test
    void testMethodMapTransformValue() {
        ParsingResult<String> result = ParsingResult.of("1");
        ParsingResult<Integer> mapped = result.map(Integer::parseInt);
        assertTrue(mapped.isPresent());
        assertEquals(1, mapped.getValue());
    }
    @Test
    void testMethodMapPropagateError() {
        ParsingResult<String> errorResult = ParsingResult.error("oh...");
        ParsingResult<Integer> mapped = errorResult.map(str -> str.length());
        assertTrue(mapped.isError());
        assertEquals("oh...", mapped.getErrorMessage());
    }
    @Test
    void testNullValue() {
        assertThrows(NullPointerException.class, () -> ParsingResult.of(null));
    }
    @Test
    void testNullMessage() {
        assertThrows(NullPointerException.class, () -> ParsingResult.error(null));
    }
}
