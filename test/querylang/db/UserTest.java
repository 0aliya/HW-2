package querylang.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void testMethodEqualsOnTheSamePerson() {
        User user = new User(0, "Майк", "Тайсон", "Хз", 60);
        assertEquals(user, user);
    }
    @Test
    void testMethodEqualsTwoIdenticalPeople() {
        User user1 = new User(10, "Хочу", "Закрыть", "Джаву", 10);
        User user2 = new User(10, "Хочу", "Закрыть", "Джаву", 10);
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }
    @Test
    void testGetters() {
        User user = new User(11, "Пикми", "Герл", "Тикток", 29);
        assertEquals(11, user.id());
        assertEquals("Пикми", user.firstName());
        assertEquals("Герл", user.lastName());
        assertEquals("Тикток", user.city());
        assertEquals(29, user.age());
    }
    @Test
    void testMethodEqualsDifferentPeople() {
        User user1 = new User(10, "Люблю", "Делать", "Дз", 23);
        User user2 = new User(14, "Люблю", "Делать", "Дз", 23);
        User user3 = new User(10, "Ненавижу", "Делать", "Дз", 23);
        assertNotEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertNotEquals(user2, null);
        assertNotEquals(user1, "string");
    }
    @Test
    void testMethodHashCode() {
        User user = new User(12, "C", "днём", "России!", 12);
        int hash1 = user.hashCode();
        int hash2 = user.hashCode();
        assertEquals(hash1, hash2);
    }
    @Test
    void testMethodToString() {
        User user = new User(10, "ля", "ляля", "ляляля", 89);
        String str = user.toString();
        assertTrue(str.contains("id=10"));
        assertTrue(str.contains("firstName=ля"));
        assertTrue(str.contains("lastName=ляля"));
        assertTrue(str.contains("city=ляляля"));
        assertTrue(str.contains("age=89"));
    }
}
