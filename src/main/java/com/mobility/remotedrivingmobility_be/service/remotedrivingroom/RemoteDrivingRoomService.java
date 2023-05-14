package com.mobility.remotedrivingmobility_be.service.remotedrivingroom;

import com.mobility.remotedrivingmobility_be.domain.remotedrivingroom.RemoteDrivingRoom;
import com.mobility.remotedrivingmobility_be.repository.client.ClientRepository;
import com.mobility.remotedrivingmobility_be.repository.remotedrivingroom.impl.RemoteDrivingRoomSearchRepository;
import com.mobility.remotedrivingmobility_be.repository.member.MemberRepository;
import com.mobility.remotedrivingmobility_be.repository.remotedrivingroom.RemoteDrivingRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

import static com.mobility.remotedrivingmobility_be.domain.member.Client.*;

@Service
@RequiredArgsConstructor
public class RemoteDrivingRoomService {
    private final RemoteDrivingRoomRepository remoteDrivingRoomRepository;
    private final RemoteDrivingRoomSearchRepository remoteDrivingRoomSearchRepository;
    private final MemberRepository memberRepository;
    private final ClientRepository clientRepository;


    public List<RemoteDrivingRoom> searchMyRoomList(Long memberId) {
        return remoteDrivingRoomSearchRepository.findAllByMemberId(memberId);
    }



    public RemoteDrivingRoom searchRoom(Long roomId) {
        return remoteDrivingRoomRepository.findById(roomId).orElseThrow(NoSuchElementException::new);
    }


    @Transactional
    public RemoteDrivingRoom joinRoom(Long memberId, Long roomId, WebSocketSession session) {
        RemoteDrivingRoom roomTryingToJoin = remoteDrivingRoomRepository.findById(roomId).orElseThrow(NoSuchElementException::new);
        roomTryingToJoin.join(
                createClient(
                        memberRepository.getReferenceById(memberId),
                        roomTryingToJoin,
                        session.getId())
        );

        return roomTryingToJoin;
    }

    @Transactional
    public void leaveRoom(String sessionId) {
        clientRepository.findBySessionId(sessionId).ifPresent(c -> {
            c.getRemoteDrivingRoom().leave(c);
            clientRepository.delete(c);
        });
    }
}
