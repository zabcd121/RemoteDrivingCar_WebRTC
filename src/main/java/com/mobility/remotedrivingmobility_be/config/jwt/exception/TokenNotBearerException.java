package com.mobility.remotedrivingmobility_be.config.jwt.exception;

public class TokenNotBearerException extends RuntimeException{
    public TokenNotBearerException(String message) {
        super(message);
    }
}
