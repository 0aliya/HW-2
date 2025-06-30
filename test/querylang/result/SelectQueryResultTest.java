package querylang.result;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class SelectQueryResultTest {
    @Test
    void testSelectEmptyList() {
        SelectQueryResult result = new SelectQueryResult(List.of());
        assertEquals("", result.message());
    }
    @Test
    void testSelectListWithInePerson() {
        List<List<String>> data = List.of(
                List.of("Аркадий", "Паравозов", "33")
        );
        SelectQueryResult result = new SelectQueryResult(data);
        assertEquals("Аркадий, Паравозов, 33", result.message());
    }
    @Test
    void testSelectListWithTwoPeople() {
        List<List<String>> data = List.of(
                List.of("Ни", "не", "2"),
                List.of("Не", "ни", "22")
        );
        SelectQueryResult result = new SelectQueryResult(data);
        String expected = "Ни, не, 2\nНе, ни, 22";
        assertEquals(expected, result.message());
    }
}
