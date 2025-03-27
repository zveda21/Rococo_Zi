package guru.qa.rococo.service;

import guru.qa.rococo.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Log4j2
@RestControllerAdvice
public class GatewayExceptionHandler extends ResponseEntityExceptionHandler {

    private static ResponseEntity<ErrorResponse> withStatus(int status, String message) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(message, status));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleApiNoResponseException(RuntimeException e, HttpServletRequest request) {
        log.warn("### Resolve Exception in @RestControllerAdvice ", e);
        return withStatus(HttpStatus.SERVICE_UNAVAILABLE.value(), e.getMessage());
    }
}
