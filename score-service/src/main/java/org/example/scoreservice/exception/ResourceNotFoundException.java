package org.example.scoreservice.exception;

public class ResourceNotFoundException extends Throwable {
    public ResourceNotFoundException(String message){
        super(message);
    }
}
