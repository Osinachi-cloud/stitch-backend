package com.stitch.user.exception;


import com.stitch.commons.enums.ResponseStatus;
import com.stitch.user.model.Error;
import org.springframework.http.HttpStatus;


public class UserNotFoundException extends UserException {


    private ResponseStatus status;
    private Error error;
    private Object details;

    public UserNotFoundException(String message){
        super(message);
    }

    public UserNotFoundException(ResponseStatus status){
        super(status.getMessage());
        this.status = status;

    }

    public UserNotFoundException(ResponseStatus status, Object details){
        super(status.getMessage());
        this.status = status;
        this.details = details;
    }

    public UserNotFoundException(String message, Throwable cause){
        super(message,cause);
    }

    public UserNotFoundException(ResponseStatus status, Throwable cause){
        super(status.getMessage(),cause);
        this.status = status;
    }

    @Override
    public String getMessage(){

        String message = details!=null?super.getMessage()+" : "+details:super.getMessage();
        return message;
    }


    public ResponseStatus getStatus() {
        return status;
    }

    public HttpStatus getHttpStatus() {
        return status.getHttpStatus();
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }


    public Error getError(){
        error = new Error();
        error.setCode(status.getCode());
        error.setMessage(status.getMessage());
        return error;
    }

}

