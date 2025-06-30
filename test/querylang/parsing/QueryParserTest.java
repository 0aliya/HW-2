package querylang.parsing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import querylang.queries.*;

import static org.junit.jupiter.api.Assertions.*;

class QueryParserTest {
    private QueryParser parser;
    @BeforeEach
    void setUp() {
        parser = new QueryParser();
    }
    @Test
    void testEmptyInput() {
        ParsingResult<Query> result = parser.parse("");
        assertTrue(result.isError());
        assertEquals("Oops, empty command!", result.getErrorMessage());
    }
    @Test
    void testInvalidQueryName() {
        ParsingResult<Query> result = parser.parse("ЧОЗАБРЕТТО");
        assertTrue(result.isError());
        assertTrue(result.getErrorMessage().contains("Unexpected query name"));
    }
    @Test
    void testMethodClearWithoutErrors() {
        ParsingResult<Query> result = parser.parse("CLEAR");
        assertFalse(result.isError());
        assertInstanceOf(ClearQuery.class, result.getValue());
    }
    @Test
    void testClearWithArgsReturnsError() {
        ParsingResult<Query> result = parser.parse("CLEAR (age)");
        assertTrue(result.isError());
        assertEquals("'CLEAR' doesn't accept arguments", result.getErrorMessage());
    }
    @Test
    void testMethodSelectWithWrongField() {
        ParsingResult<Query> result = parser.parse("SELECT (error)");
        assertTrue(result.isError());
        assertEquals("Oops, invalid field in SELECT!", result.getErrorMessage());
    }
    @Test
    void testMethodSelectWithOrderWithInvalidSecondArgument() {
        ParsingResult<Query> result = parser.parse("SELECT * ORDER(age, по возрастанию)");
        assertTrue(result.isError());
        assertTrue(result.getErrorMessage().contains("ORDER sorts in ascending (ASC) or descending (DESC) order"));
    }
    @Test
    void testMethodSelectWithFilter() {
        ParsingResult<Query> result = parser.parse("SELECT * FILTER(city, Moscow)");
        assertFalse(result.isError());
        assertInstanceOf(SelectQuery.class, result.getValue());
    }
    @Test
    void testMethodSelectWithOrderWithoutErrors() {
        ParsingResult<Query> result = parser.parse("SELECT (age) ORDER(age, ASC)");
        assertFalse(result.isError());
        assertInstanceOf(SelectQuery.class, result.getValue());
    }
    @Test
    void testMethodInsertWithoutErrors() {
        ParsingResult<Query> result = parser.parse("INSERT (Тур, Пу, Гор, 1)");
        assertFalse(result.isError());
        assertInstanceOf(InsertQuery.class, result.getValue());
    }
    @Test
    void testInsertWithInvalidAge() {
        ParsingResult<Query> result = parser.parse("INSERT (Дом, Дом, Дом, пятьдесят)");
        assertTrue(result.isError());
        assertEquals("Oops, age must be an integer!", result.getErrorMessage());
    }
    @Test
    void testMethodInsertWithEmptyArgument() {
        ParsingResult<Query> result = parser.parse("INSERT ()");
        assertTrue(result.isError());
    }
    @Test
    void testMethodInsertWithoutParentheses() {
        ParsingResult<Query> result = parser.parse("INSERT имя, фамилия, город, 2");
        assertTrue(result.isError());
        assertEquals("Oops, INSERT takes arguments in parentheses!", result.getErrorMessage());
    }
    @Test
    void testMethodRemoveWithInvalidId() {
        ParsingResult<Query> result = parser.parse("REMOVE первого");
        assertTrue(result.isError());
        assertEquals("Oops, REMOVE only takes an integer id!", result.getErrorMessage());
    }
    @Test
    void testMethodRemoveWithoutErrors() {
        ParsingResult<Query> result = parser.parse("REMOVE 1");
        assertFalse(result.isError());
        assertInstanceOf(RemoveQuery.class, result.getValue());
    }
}
