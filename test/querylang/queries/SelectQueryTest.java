package querylang.queries;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import querylang.db.Database;
import querylang.db.User;
import querylang.result.SelectQueryResult;
import querylang.util.FieldGetter;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import static org.junit.jupiter.api.Assertions.*;

class SelectQueryTest {
    private Database db;
    @BeforeEach
    void setUp() {
        db = new Database();
        db.add(new User(0, "Айгуль", "Нормис", "Казань", 14));
        db.add(new User(0, "Марат", "Норми", "Казань2", 16));
        db.add(new User(0, "Вова", "Адидас", "Казань3", 19));
        db.add(new User(0, "Пальто", "Норм", "Казань4", 21));
    }
    @Test
    void testSelectEmptyResult() {
        List<FieldGetter> getters = List.of(User::firstName);
        Predicate<User> predicate = user -> user.age() > 10030388; // no match
        Comparator<User> comparator = Comparator.comparing(User::id);
        SelectQuery query = new SelectQuery(getters, predicate, comparator);
        SelectQueryResult result = query.execute(db);
        assertEquals("", result.message());
    }
    @Test
    void testSelectFilteredByCity() {
        List<FieldGetter> getters = List.of(User::firstName, User::city);
        Predicate<User> predicate = user -> user.city().equals("Казань");
        Comparator<User> comparator = Comparator.comparing(User::age).reversed();
        SelectQuery query = new SelectQuery(getters, predicate, comparator);
        SelectQueryResult result = query.execute(db);
        String output = result.message();
        assertTrue(output.contains("Айгуль"));
        assertFalse(output.contains("Марат"));
    }
    @Test
    void testSelectAllWithAllFields() {
        List<FieldGetter> getters = List.of(
                user -> String.valueOf(user.id()),
                User::firstName,
                User::lastName,
                User::city,
                user -> String.valueOf(user.age())
        );
        Predicate<User> predicate = user -> true; // select all
        Comparator<User> comparator = Comparator.comparing(User::id);
        SelectQuery query = new SelectQuery(getters, predicate, comparator);
        SelectQueryResult result = query.execute(db);
        String output = result.message();
        assertTrue(output.contains("Айгуль"));
        assertTrue(output.contains("Марат"));
        assertTrue(output.contains("Вова"));
        assertTrue(output.contains("Пальто"));
        assertEquals(4, output.split("\n").length);
    }
}
