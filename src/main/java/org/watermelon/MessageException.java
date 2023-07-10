package org.watermelon;

public class MessageException extends RuntimeException{
    public MessageException(String errorMessage){
        super(errorMessage);
    }
}