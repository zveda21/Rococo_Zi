package guru.qa.rococo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotFondException extends RuntimeException {
    public NotFondException(String message) {
        super(message);
    }
}
