package com.stitch.gateway.exception;


import com.stitch.gateway.model.ErrorResponse;
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class AppExceptionHandler {


    @ExceptionHandler
//    protected ResponseEntity<JvmType.Object> handleException(Throwable e){
    protected ResponseEntity<ErrorResponse> handleException(Throwable e){
        log.error(e.getMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(2);
        errorResponse.setMessage("Error processing request");

        return ResponseEntity.badRequest().body(errorResponse);
    }
}
