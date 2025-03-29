package guru.qa.rococo.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
