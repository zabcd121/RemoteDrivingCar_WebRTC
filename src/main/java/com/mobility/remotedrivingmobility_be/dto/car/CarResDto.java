package com.mobility.remotedrivingmobility_be.dto.car;

import com.mobility.remotedrivingmobility_be.domain.car.Car;
import lombok.*;

public class CarResDto {

    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @Getter
    public static class CarSearchRes {
        private Long carId;
        private String carNumber;

        public static CarSearchRes of(Car car) {
            return CarSearchRes.builder()
                    .carId(car.getId())
                    .carNumber(car.getNumber())
                    .build();
        }
    }
}
