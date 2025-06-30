package querylang.result;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InsertQueryResultTest {
    @Test
    void testDefaultInsert() {
        InsertQueryResult result = new InsertQueryResult(38337);
        String actual = result.message();
        assertEquals("User with id 38337 was added successfully", actual);
    }
}
