package querylang.queries;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import querylang.db.Database;
import querylang.db.User;
import querylang.result.RemoveQueryResult;
import static org.junit.jupiter.api.Assertions.*;
class RemoveQueryTest {
    private Database db;
    @BeforeEach
    void setUp() {
        db = new Database();
    }
    @Test
    void testRemoveExistUser() {
        int userId = db.add(new User(0, "Захар", "Крамбл", "Куки", 21));
        RemoveQuery query = new RemoveQuery(userId);
        RemoveQueryResult result = query.execute(db);
        assertTrue(result.message().contains("was removed successfully"));
        assertEquals(0, db.size(), "он должен был быть удален из бд, а болше никого там и нет");
    }
    @Test
    void testRemoveNoExistUser() {
        RemoveQuery query = new RemoveQuery(1);
        RemoveQueryResult result = query.execute(db);
        assertFalse(result.message().contains("was removed successfully"));
        assertTrue(result.message().contains("not exists"), "сообщение об ошибке должно содержать об этом инфу");
        assertEquals(0, db.size(), "БД как была, так и осталась пустой");
    }
    @Test
    void testRemoveTheSameUserDouble() {
        int id = db.add(new User(4, "ноунейм", "м", "бавлы", 3));
        RemoveQuery firstAttempt = new RemoveQuery(id);
        RemoveQuery secondAttempt = new RemoveQuery(id);
        RemoveQueryResult firstResult = firstAttempt.execute(db);
        RemoveQueryResult secondResult = secondAttempt.execute(db);
        assertEquals("User with id " + id + " was removed successfully", firstResult.message());
        assertEquals("Error! User with id " + id + " not exists...", secondResult.message());
        assertEquals(0, db.size(), "после первого удаления БД должна стать пустой");
    }
}
