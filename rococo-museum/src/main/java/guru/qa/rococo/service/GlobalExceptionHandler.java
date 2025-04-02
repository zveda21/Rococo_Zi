package guru.qa.rococo.service;

import guru.qa.rococo.exception.NotFoundException;
import guru.qa.rococo.model.ErrorJson;
import jakarta.annotation.Nonnull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorJson> handleNotFoundException(@Nonnull NotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorJson(
                        new Date(),
                        HttpStatus.NOT_FOUND.value(),
                        List.of("Object not found in the database.", ex.getMessage())
                ));
    }
}