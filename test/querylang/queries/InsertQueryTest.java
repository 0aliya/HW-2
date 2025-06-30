package querylang.queries;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import querylang.db.Database;
import querylang.db.User;
import querylang.result.InsertQueryResult;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InsertQueryTest {
    private Database db;
    @BeforeEach
    void setUp() {
        db = new Database();
    }
    @Test
    void testInsertOneUser() {
        User user = new User(0, "Володя", "Мирный", "Париж", 3652);
        InsertQuery query = new InsertQuery(user);
        InsertQueryResult result = query.execute(db);
        assertEquals("User with id 0 was added successfully", result.message());
        List<User> users = db.getAll();
        assertEquals(1, users.size());
        User stored = users.get(0);
        assertEquals(0, stored.id());
        assertEquals("Володя", stored.firstName());
        assertEquals("Мирный", stored.lastName());
        assertEquals("Париж", stored.city());
        assertEquals(3652, stored.age());
    }

    @Test
    void testInsertionsGenerateSequentialIdsTwoPeople() {
        InsertQuery q1 = new InsertQuery(new User(0, " Кот", "Борис", "Зоопарк", 1));
        InsertQuery q2 = new InsertQuery(new User(0, "Собака", "Мухтар", "Зоопарк", 3));
        InsertQueryResult r1 = q1.execute(db);
        InsertQueryResult r2 = q2.execute(db);
        assertEquals("User with id 0 was added successfully", r1.message());
        assertEquals("User with id 1 was added successfully", r2.message());
        List<User> users = db.getAll();
        assertEquals(2, users.size());
        assertEquals(0, users.get(0).id());
        assertEquals(1, users.get(1).id());
    }
}
