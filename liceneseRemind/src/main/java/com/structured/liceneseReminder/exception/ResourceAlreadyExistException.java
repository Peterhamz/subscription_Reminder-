package com.structured.liceneseReminder.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.ALREADY_REPORTED)
public class ResourceAlreadyExistException extends RuntimeException{

   // private HttpStatus status;
    private String message;

    public ResourceAlreadyExistException( String message) {
        //this.status = status;
        this.message = message;
    }

    public ResourceAlreadyExistException(String message, String message1) {
        super(message);
      //  this.status = status;
        this.message = message1;
    }

//    public HttpStatus getStatus() {
//        return status;
//    }
}