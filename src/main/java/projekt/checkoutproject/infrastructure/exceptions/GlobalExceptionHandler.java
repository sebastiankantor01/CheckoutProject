package projekt.checkoutproject.infrastructure.exceptions;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Map<Class<?>, HttpStatus> EXCEPTION_STATUS;

    static {
        EXCEPTION_STATUS = new HashMap<>();
        EXCEPTION_STATUS.put(ConstraintViolationException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS.put(MethodArgumentNotValidException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS.put(BindException.class, HttpStatus.BAD_REQUEST);

        EXCEPTION_STATUS.put(InvalidPasswordException.class, HttpStatus.UNAUTHORIZED);
        EXCEPTION_STATUS.put(UnknownUserException.class, HttpStatus.UNAUTHORIZED);
        EXCEPTION_STATUS.put(UserAlreadyExistsException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS.put(UserNotFoundException.class, HttpStatus.NOT_FOUND);

        EXCEPTION_STATUS.put(ProductNotFoundException.class, HttpStatus.NOT_FOUND);

        EXCEPTION_STATUS.put(ResourceNotFoundException.class, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            @Nullable Object body,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        final String errorId = UUID.randomUUID().toString();
        log.error("Exception: ID={}, HttpStatus={}", errorId, status, ex);

        return super.handleExceptionInternal(
                ex,
                ExceptionMessage.of(errorId, ex.getMessage()),
                headers,
                status,
                request
        );
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        final String errorId = UUID.randomUUID().toString();
        log.error("Exception: ID={}, HttpStatus={}", errorId, status, ex);

        String message = String.format("Bad request for field: [%s], wrong value: [%s]",
                Optional.ofNullable(ex.getFieldError()).map(FieldError::getField).orElse("unknown"),
                Optional.ofNullable(ex.getFieldError()).map(FieldError::getRejectedValue).orElse("unknown"));

        return ResponseEntity
                .status(getHttpStatusFromException(ex.getClass()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(ExceptionMessage.of(errorId, message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handle(Exception exception) {
        return doHandle(exception, getHttpStatusFromException(exception.getClass()));
    }

    private HttpStatus getHttpStatusFromException(final Class<?> exceptionClass) {
        return EXCEPTION_STATUS.getOrDefault(exceptionClass, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<?> doHandle(Exception exception, HttpStatus status) {
        final String errorId = UUID.randomUUID().toString();
        log.error("Exception: ID={}, HttpStatus={}", errorId, status, exception);

        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ExceptionMessage.of(errorId, exception.getMessage()));
    }
}
