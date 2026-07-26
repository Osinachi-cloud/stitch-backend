package com.stitch.gateway.exception;


import com.stitch.commons.exception.StitchException;
import com.stitch.exception.CartException;
import com.stitch.exception.OrderException;
import com.stitch.exception.ProductException;
import com.stitch.gateway.model.response.ErrorResponse;
import com.stitch.payment.exception.PaymentException;
import com.stitch.user.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.stitch.commons.util.Constants.status;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@ControllerAdvice
public class GatewayCustomExceptionHandler {


    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleException(Throwable e) {
        log.error(e.getMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(2);
        String details = e.getMessage() != null ? e.getMessage() : "No details available";
        errorResponse.setMessage(e.getClass().getSimpleName() + ": " + details);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, String>> handleApiExceptions(ApiException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "API error";
        return new ResponseEntity<>(Map.of("error", message), status(ex.getCode()));
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Invalid credentials";
        return new ResponseEntity<>(Map.of("error", message), UNAUTHORIZED);
    }
    @ExceptionHandler(OrderException.class)
    public ResponseEntity<Map<String, String>> handleOrderException(OrderException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Order processing error";
        return new ResponseEntity<>(Map.of("error", message), status(ex.getCode()));
    }
    @ExceptionHandler(ProductException.class)
    public ResponseEntity<Map<String, String>> handleProductException(ProductException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Product processing error";
        return new ResponseEntity<>(Map.of("error", message), status(ex.getCode()));
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<Map<String, String>> handlePaymentException(PaymentException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Payment processing error";
        return new ResponseEntity<>(Map.of("error", message), status(ex.getCode()));
    }

    @ExceptionHandler(CartException.class)
    public ResponseEntity<Map<String, String>> handleCartException(CartException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Cart processing error";
        return new ResponseEntity<>(Map.of("error", message), status(ex.getCode()));
    }
    @ExceptionHandler(UserException.class)
    public ResponseEntity<Map<String, String>> handleUserException(UserException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "User processing error";
        if (Objects.nonNull(ex.getStatus())){
            return new ResponseEntity<>(Map.of("error", message), ex.getStatus().getHttpStatus());
        }
        if (ex.getCode() >= 200){
            return new ResponseEntity<>(Map.of("error", message), status(ex.getCode()));
        }
        return new ResponseEntity<>(Map.of("error", message), BAD_REQUEST);
    }


    @ExceptionHandler(StitchException.class)
    public ResponseEntity<Map<String, String>> handleStitchException(StitchException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "An error occurred";
        return new ResponseEntity<>(Map.of("error", message), BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleInvalidMethodArgument(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        log.error("Method arguments not valid ==> {}", ex.getMessage());
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("parameterName", ex.getName());
        errors.put("parameterValue", Objects.isNull(ex.getValue()) ? "" : String.valueOf(ex.getValue()));
        errors.put("message", ex.getMessage());
        return new ResponseEntity<>(errors, BAD_REQUEST);
    }
}
