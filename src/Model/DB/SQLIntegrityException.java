package Model.DB;

public class SQLIntegrityException extends RuntimeException {

    public SQLIntegrityException(String msg) {
        super(msg);
    }
}
