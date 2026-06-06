package com.url_shortner.url_shortner.exception;

public class UrlNotAccessibleException extends RuntimeException{
    public UrlNotAccessibleException(String message){
        super(message);
    }
}
