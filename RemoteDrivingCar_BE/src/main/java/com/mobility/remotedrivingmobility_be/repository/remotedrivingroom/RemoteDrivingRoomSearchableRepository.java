package com.mobility.remotedrivingmobility_be.repository.remotedrivingroom;

import com.mobility.remotedrivingmobility_be.domain.remotedrivingroom.RemoteDrivingRoom;

import java.util.List;

public interface RemoteDrivingRoomSearchableRepository {
    List<RemoteDrivingRoom> findAllByMemberId(Long memberId);

    RemoteDrivingRoom findByCarId(Long carId);
}
