package com.mobility.remotedrivingmobility_be.config.jwt.exception;

public class TokenNotPassedException extends RuntimeException {
    public TokenNotPassedException(String message) {
        super(message);
    }
}
