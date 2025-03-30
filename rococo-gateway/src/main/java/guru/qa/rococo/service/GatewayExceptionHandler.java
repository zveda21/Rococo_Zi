package guru.qa.rococo.service;

import guru.qa.rococo.exception.InvalidRequestException;
import guru.qa.rococo.exception.RemoteServerException;
import guru.qa.rococo.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GatewayExceptionHandler extends ResponseEntityExceptionHandler {

    private static ResponseEntity<ErrorResponse> withStatus(int status, String message) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(message, status));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleApiErrorResponse(RuntimeException e, HttpServletRequest request) {
        return switch (e) {
            case InvalidRequestException invalid -> withStatus(HttpStatus.BAD_REQUEST.value(), invalid.getMessage());
            case RemoteServerException remoteServerException -> {
                log.error("Remote server exception", remoteServerException.getCause());
                yield withStatus(remoteServerException.getStatus().value(), remoteServerException.getMessage());
            }
            default -> withStatus(HttpStatus.SERVICE_UNAVAILABLE.value(), e.getMessage());
        };
    }
}
