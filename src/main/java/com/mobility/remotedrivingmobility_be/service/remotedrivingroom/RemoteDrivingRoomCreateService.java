package com.mobility.remotedrivingmobility_be.service.remotedrivingroom;

import com.mobility.remotedrivingmobility_be.domain.remotedrivingroom.RemoteDrivingRoom;
import com.mobility.remotedrivingmobility_be.repository.car.CarRepository;
import com.mobility.remotedrivingmobility_be.repository.remotedrivingroom.RemoteDrivingRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RemoteDrivingRoomCreateService {

    private final RemoteDrivingRoomRepository remoteDrivingRoomRepository;
    private final CarRepository carRepository;

    @Transactional
    public void createRoom(Long carId) {
        remoteDrivingRoomRepository.save(
                RemoteDrivingRoom.createRemoteDrivingRoom(
                        carRepository.findById(carId).orElseThrow(NoSuchElementException::new)
                )
        );
    }
}
