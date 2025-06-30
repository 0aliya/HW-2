package querylang.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
    private Database db;
    @BeforeEach
    void setUp() {
        db = new Database();
    }
    @Test
    void testMethodsAddAndGetAll() {
        User user1 = new User(10, "Aliya", "Valiullina", "Moscow", 18);
        int id1 = db.add(user1);
        assertEquals(0, id1);

        User user2 = new User(1029, "Pen", "Blue", "PencilCase", 307);
        int id2 = db.add(user2);
        assertEquals(1, id2);

        List<User> users = db.getAll();
        assertEquals(2, users.size());

        //проверяем, что пользователи добавлены с корректными id
        assertEquals(0, users.get(0).id());
        assertEquals("Aliya", users.get(0).firstName());

        assertEquals(1, users.get(1).id());
        assertEquals("Pen", users.get(1).firstName());
    }
    @Test
    void testRemoveEmptyList() {
        boolean removed = db.remove(0);
        assertFalse(removed);
    }
    @Test
    void testRemoveWithoutErrors() {
        User user = new User(78, "Стакан", "Бокалович", "Стол", 1);
        int id = db.add(user);
        boolean removed = db.remove(id);
        assertTrue(removed);
        assertEquals(0, db.size());
    }
    @Test
    void testMethodsSizeAndClear() {
        assertEquals(0, db.size());
        db.add(new User(-1, "Bob", "Marley", "Kingston", 36));
        assertEquals(1, db.size());
        db.clear();
        assertEquals(0, db.size());
    }
}
