package guru.qa.rococo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
