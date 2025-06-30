package querylang.result;

import java.util.List;
import java.util.stream.Collectors;

public class SelectQueryResult implements QueryResult {
    private final List<List<String>> selectedValues;

    public SelectQueryResult(List<List<String>> selectedValues) {
        this.selectedValues = List.copyOf(selectedValues);
    }

    @Override
    public String message() {
        //return null был "временной заглушкой" поэтому был убран
        return selectedValues.stream()
                .map(row -> String.join(", ", row))
                .collect(Collectors.joining("\n"));
    }
}