package guru.qa.rococo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;


public class RemoteServerException extends RuntimeException {
    public RemoteServerException(RestClientException cause) {
        // does not expose details to the API
        super("Remote API execution failed", cause);
    }

    public HttpStatusCode getStatus() {
        return switch (getCause()) {
            case HttpStatusCodeException httpStatusCodeException -> httpStatusCodeException.getStatusCode();
            default -> HttpStatus.SERVICE_UNAVAILABLE;
        };
    }
}
