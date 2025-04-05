package com.stitch.gateway.exception;


import com.stitch.exception.CartException;
import com.stitch.exception.OrderException;
import com.stitch.exception.ProductException;
import com.stitch.gateway.model.response.ErrorResponse;
import com.stitch.payment.exception.PaymentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

@Slf4j
@ControllerAdvice
public class AppExceptionHandler {


    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleException(Throwable e) {
        log.error(e.getMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(2);
        errorResponse.setMessage("Error processing request");

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, String>> handleApiExceptions(ApiException ex) {
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), status(ex.getCode()));
    }
    @ExceptionHandler(OrderException.class)
    public ResponseEntity<Map<String, String>> handleOrderException(OrderException ex) {
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), status(ex.getCode()));
    }
    @ExceptionHandler(ProductException.class)
    public ResponseEntity<Map<String, String>> handleProductException(ProductException ex) {
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), status(ex.getCode()));
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<Map<String, String>> handlePaymentException(PaymentException ex) {
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), status(ex.getCode()));
    }

    @ExceptionHandler(CartException.class)
    public ResponseEntity<Map<String, String>> handleCartException(CartException ex) {
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), status(ex.getCode()));
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
