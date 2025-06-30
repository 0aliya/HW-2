package querylang.parsing;

import querylang.db.User;
import querylang.queries.*;
import querylang.util.FieldGetter;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class QueryParser {
    private static final Set<String> VALID_FIELDS = Set.of("id", "firstName", "lastName", "city", "age");
    private static final List<String> ALL_FIELDS = List.of("id", "firstName", "lastName", "city", "age");
    public ParsingResult<Query> parse(String line) {
        String[] parts = line.strip().split("\\s+", 2);
        if (parts.length == 0 || parts[0].isBlank()) {
            return ParsingResult.error("Oops, empty command!");
        }
        String queryName = parts[0].strip().toUpperCase();
        String arguments = parts.length > 1 ? parts[1].strip() : "";
        return switch (queryName) {
            case "CLEAR" -> parseClear(arguments);
            case "SELECT" -> parseSelect(arguments);
            case "INSERT" -> parseInsert(arguments);
            case "REMOVE" -> parseRemove(arguments);
            default -> ParsingResult.error("Unexpected query name '" + queryName + "'");
        };
    }

    private ParsingResult<Query> parseSelect(String rest) {
        List<FieldGetter> getters;
        if (rest.startsWith("*")) {
            getters = createFieldGetters(ALL_FIELDS);
            rest = rest.substring(1).strip();
        } else if (rest.startsWith("(")) {
            int endIdx = rest.indexOf(')');
            if (endIdx == -1) {
                return ParsingResult.error("Oops, there is no closing parenthesis in the field list :(");
            }
            String inside = rest.substring(1, endIdx);
            if (inside.contains(",") && !inside.contains(", ")) {
                return ParsingResult.error("Oops, invalid argument separator: expected ', ' as field separator :(");
            }

            List<String> fields = Arrays.stream(inside.split(", "))
                    .map(String::strip)
                    .toList();

            if (fields.isEmpty()){
                return ParsingResult.error("Oops, empty field list!");
            }
            if (!VALID_FIELDS.containsAll(fields)){
                return ParsingResult.error("Oops, invalid field in SELECT!");
            }

            getters = createFieldGetters(fields);
            rest = rest.substring(endIdx + 1).strip();
        } else {
            return ParsingResult.error("Oops, invalid symbol after SELECT: expected '*' or ' (' ");
        }
        Predicate<User> predicate = u -> true;
        Comparator<User> comparator = Comparator.comparingInt(User::id);
        boolean comparatorSet = false;
        while (!rest.isEmpty()) {
            if (rest.toUpperCase().startsWith("FILTER(")) {
                int end = rest.indexOf(')', 7);
                if (end == -1){
                    return ParsingResult.error("Oops, there is no closing parenthesis in FILTER");
                }
                String content = rest.substring(7, end);
                if (!content.contains(", ")){
                    return ParsingResult.error("Oops, FILTER only takes ', ' separator");
                }
                String[] parts = content.split(", ");
                if (parts.length != 2){
                    return ParsingResult.error("Oops, FILTER only takes 2 arguments!");
                }
                String field = parts[0].strip();
                String value = parts[1].strip();
                if (!VALID_FIELDS.contains(field)){
                    return ParsingResult.error("Oops, invalid field in FILTER");
                }
                Predicate<User> f = createFilter(field, value);
                if (f == null){
                    return ParsingResult.error("Oops, invalid filter value!");
                }
                predicate = predicate.and(f);
                rest = rest.substring(end + 1).strip();
            } else if (rest.toUpperCase().startsWith("ORDER(")) {
                if (comparatorSet){
                    return ParsingResult.error("Oops, multiple ORDER clauses!");
                }
                int end = rest.indexOf(')', 6);
                if (end == -1){
                    return ParsingResult.error("Oops, there is no closing parenthesis in ORDER");
                }
                String content = rest.substring(6, end);
                if (!content.contains(", ")){
                    return ParsingResult.error("Oops, ORDER only takes ', ' separator");
                }
                String[] parts = content.split(", ");
                if (parts.length != 2){
                    return ParsingResult.error("Oops, ORDER only takes 2 acceptable arguments");
                }
                String field = parts[0].strip();
                String direction = parts[1].strip().toUpperCase();
                if (!VALID_FIELDS.contains(field)){
                    return ParsingResult.error("Oops, invalid field in ORDER!");
                }
                Comparator<User> comp = createComparator(field);
                if (comp == null){
                    return ParsingResult.error("Oops, invalid sort option!");
                }
                if ("DESC".equals(direction)) comp = comp.reversed();
                else if (!"ASC".equals(direction)){
                    return ParsingResult.error("Oops, ORDER sorts in ascending (ASC) or descending (DESC) order!");
                }
                comparator = comp.thenComparingInt(User::id);
                comparatorSet = true;
                rest = rest.substring(end + 1).strip();
            } else {
                return ParsingResult.error("Oops, unexpected part in SELECT query: " + rest);
            }
        }
        return ParsingResult.of(new SelectQuery(getters, predicate, comparator));
    }

    private ParsingResult<Query> parseInsert(String rest) {
        if (!rest.startsWith("(") || !rest.endsWith(")")){
            return ParsingResult.error("Oops, INSERT takes arguments in parentheses!");
        }
        String content = rest.substring(1, rest.length() - 1);
        if (!content.contains(", ")) return ParsingResult.error(" Oops, INSERT only takes ', ' as field separator!");
        String[] parts = content.split(", ");
        if (parts.length != 4) return ParsingResult.error("Oops, INSERT takes only 4 acceptable fields!");
        String firstName = parts[0].strip();
        String lastName = parts[1].strip();
        String city = parts[2].strip();
        String ageStr = parts[3].strip();
        if (firstName.isBlank() || lastName.isBlank() || city.isBlank()) {
            return ParsingResult.error("Oops, firstName, lastName and city must not be empty");
        }
        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            return ParsingResult.error("Oops, age must be an integer!");
        }
        if (age < 0) return ParsingResult.error("Oops, age must be non-negative!");
        User user = new User(0, firstName.strip(), lastName.strip(), city.strip(), age);
        return ParsingResult.of(new InsertQuery(user));
    }

    private ParsingResult<Query> parseRemove(String rest) {
        if (!rest.matches("\\d+")) return ParsingResult.error("Oops, REMOVE only takes an integer id!");
        return ParsingResult.of(new RemoveQuery(Integer.parseInt(rest)));
    }

    private ParsingResult<Query> parseClear(String arguments) {
        if (!arguments.isEmpty()) {
            return ParsingResult.error("'CLEAR' doesn't accept arguments");
        }
        return ParsingResult.of(new ClearQuery());
    }

    private List<FieldGetter> createFieldGetters(List<String> names) {
        Map<String, Function<User, String>> map = Map.of(
                "id", u -> String.valueOf(u.id()),
                "firstName", User::firstName,
                "lastName", User::lastName,
                "city", User::city,
                "age", u -> String.valueOf(u.age())
        );
        return names.stream().map(f -> (FieldGetter) user -> map.get(f).apply(user)).toList();
    }

    private Predicate<User> createFilter(String field, String value) {
        try {
            return switch (field) {
                case "id" -> u -> u.id() == Integer.parseInt(value);
                case "age" -> u -> u.age() == Integer.parseInt(value);
                case "firstName" -> u -> u.firstName().equals(value);
                case "lastName" -> u -> u.lastName().equals(value);
                case "city" -> u -> u.city().equals(value);
                default -> null;
            };
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Comparator<User> createComparator(String field) {
        return switch (field) {
            case "id" -> Comparator.comparingInt(User::id);
            case "firstName" -> Comparator.comparing(User::firstName);
            case "lastName" -> Comparator.comparing(User::lastName);
            case "city" -> Comparator.comparing(User::city);
            case "age" -> Comparator.comparingInt(User::age);
            default -> null;
        };
    }
}
