package com.mobility.remotedrivingmobility_be.dto.car;

import lombok.*;

public class CarReqDto {

    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @Getter
    public static class CarRegistrationReq2 {
        private String carNumber;
    }

    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @Getter
    public static class CarRegistrationReq {
        private String carNumber;
    }
}
