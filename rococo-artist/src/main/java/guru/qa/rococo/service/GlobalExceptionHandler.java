package guru.qa.rococo.service;

import guru.qa.rococo.model.ErrorJson;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import guru.qa.rococo.ex.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorJson> handleNotFoundException(@Nonnull NotFoundException ex) {
        LOG.warn("### Resolve Exception in @RestControllerAdvice ", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorJson(
                        new Date(),
                        HttpStatus.NOT_FOUND.value(),
                        List.of("Object not found in the database.", ex.getMessage())
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorJson> handleIllegalArgumentException(@Nonnull IllegalArgumentException ex) {
        LOG.warn("### Resolve Exception in @RestControllerAdvice ", ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorJson(
                        new Date(),
                        HttpStatus.BAD_REQUEST.value(),
                        List.of("Invalid request parameters.", ex.getMessage())
                ));
    }
}
