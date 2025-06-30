package querylang.result;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class ClearQueryResultTest {
    @Test
    void testEmptyDatabase() {
        ClearQueryResult result = new ClearQueryResult(0);
        String expected = "0 users were removed successfully";
        assertEquals(expected, result.message());
    }
    @Test
    void testNoEmptyDatabase() {
        ClearQueryResult result = new ClearQueryResult(2500);
        String expected = "2500 users were removed successfully";
        assertEquals(expected, result.message());
    }
}
