package com.mobility.remotedrivingmobility_be.repository.remotedrivingroom;

import com.mobility.remotedrivingmobility_be.domain.remotedrivingroom.RemoteDrivingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RemoteDrivingRoomRepository extends JpaRepository<RemoteDrivingRoom, Long> {

    @Query("select r from RemoteDrivingRoom r where r.car.id = :carId")
    RemoteDrivingRoom findByCarId(Long carId);

}
