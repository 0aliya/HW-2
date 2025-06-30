package querylang.result;

public class RemoveQueryResult implements QueryResult {
    private final int id;
    private final boolean success;

    public RemoveQueryResult(int id, boolean success) {
        this.id = id;
        this.success = success;
    }

    @Override
    public String message() {
        //return null был "временной заглушкой" поэтому был убран
        return success
                ? "User with id " + id + " was removed successfully"
                : "Error! User with id " + id + " not exists...";
    }
}

