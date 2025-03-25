package guru.qa.rococo.exception;

public class NoResponseException extends RuntimeException {
    public NoResponseException(String message) {
        super(message);
    }

    public NoResponseException(String message, Exception cause) {
        super(message, cause);
    }
}
