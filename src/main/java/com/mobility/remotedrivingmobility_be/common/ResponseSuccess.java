package com.mobility.remotedrivingmobility_be.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ResponseSuccess<T> {
    private String code;
    private String message;
    private T result;

    public ResponseSuccess(ResConditionCode conditionCode, T result) {
        this.code = conditionCode.getCode();
        this.message = conditionCode.getMessage();
        this.result = result;
    }
}