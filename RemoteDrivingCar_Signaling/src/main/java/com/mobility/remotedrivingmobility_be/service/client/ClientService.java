package com.mobility.remotedrivingmobility_be.service.client;

import com.mobility.remotedrivingmobility_be.domain.member.Client;
import com.mobility.remotedrivingmobility_be.domain.member.Member;
import com.mobility.remotedrivingmobility_be.domain.remotedrivingroom.RemoteDrivingRoom;
import com.mobility.remotedrivingmobility_be.repository.client.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.mobility.remotedrivingmobility_be.domain.member.Client.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClientService {
    private final ClientRepository clientRepository;

    public void addClient(Member member, RemoteDrivingRoom room, String session) {
        clientRepository.save(createClient(member, room, session));
    }

    public Client searchClientsByRoom(Long roomId) {
        return clientRepository.findByRoomId(roomId);
    }

    public Client searchClientsByCar(Long carId) {
        return clientRepository.findByCarId(carId);
    }

    public Client searchClientBySessionId(String sessionId) {
        Client client = clientRepository.findBySessionId(sessionId).orElseThrow(() -> new IllegalArgumentException("해당 세션에 해당하는 클라이언트가 존재하지 않습니다."));
        client.getRemoteDrivingRoom();
        return client;
    }

    public Client searchClientByMemberId(Long memberId) {
        Client client = clientRepository.findByMemberId(memberId);
        client.getRemoteDrivingRoom();
        return client;
    }
}
