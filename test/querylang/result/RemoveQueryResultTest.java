package querylang.result;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RemoveQueryResultTest {
    @Test
    void testRemoveExistPerson() {
        RemoveQueryResult result = new RemoveQueryResult(321, true);
        String msg = result.message();
        assertEquals("User with id 321 was removed successfully", msg);
    }
    @Test
    void testRemoveNoExistPerson() {
        RemoveQueryResult result = new RemoveQueryResult(1, false);
        String msg = result.message();
        assertEquals("Error! User with id 1 not exists...", msg);
    }
}
