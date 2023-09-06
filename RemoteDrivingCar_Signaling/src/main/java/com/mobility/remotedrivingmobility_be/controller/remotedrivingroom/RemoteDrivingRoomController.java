package com.mobility.remotedrivingmobility_be.controller.remotedrivingroom;

import com.mobility.remotedrivingmobility_be.service.remotedrivingroom.RemoteDrivingRoomCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.mobility.remotedrivingmobility_be.dto.remotedrivingroom.RemoteDrivingRoomRequestDto.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RemoteDrivingRoomController {

    private final RemoteDrivingRoomCreateService roomCreateService;

    @PostMapping
    public ResponseEntity createRoom(@RequestBody @Valid RoomCreateRequest request) {
        roomCreateService.createRoom(request.getCarId());
        return ResponseEntity.ok().build();
    }

}
