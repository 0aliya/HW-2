package querylang.queries;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import querylang.db.Database;
import querylang.db.User;
import querylang.result.ClearQueryResult;
import static org.junit.jupiter.api.Assertions.*;

class ClearQueryTest {
    private Database db;
    @BeforeEach
    void setUp() {
        db = new Database();
    }
    @Test
    void testClearEmptyDatabase() {
        ClearQuery query = new ClearQuery();
        ClearQueryResult result = query.execute(db);
        assertEquals(0, db.size(), "Clear makes database empty");
        assertEquals("0 users were removed successfully", result.message());
    }
    @Test
    void testClearNoEmptyDatabase() {
        db.add(new User(43, "Вася", "Ваисльев", "Норильск", 8));
        db.add(new User(21, "Миша", "Молодов", "Альметьевск", 5));
        ClearQuery query = new ClearQuery();
        ClearQueryResult result = query.execute(db);
        assertEquals(0, db.size(), "Clear makes database empty");
        assertEquals("2 users were removed successfully", result.message());
    }
}
