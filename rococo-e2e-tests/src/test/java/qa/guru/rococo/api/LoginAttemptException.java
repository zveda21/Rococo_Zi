package qa.guru.rococo.api;

public class LoginAttemptException extends RuntimeException {
    public LoginAttemptException() {
        super("Login attempt unsuccessful");
    }
}
