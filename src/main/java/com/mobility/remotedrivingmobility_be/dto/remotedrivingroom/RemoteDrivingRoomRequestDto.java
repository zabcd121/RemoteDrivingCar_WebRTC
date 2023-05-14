package com.mobility.remotedrivingmobility_be.dto.remotedrivingroom;

import lombok.*;

import javax.validation.constraints.NotNull;

public class RemoteDrivingRoomRequestDto {

    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @Getter
    public static class RoomCreateRequest{
        @NotNull
        private Long carId;
    }

}
