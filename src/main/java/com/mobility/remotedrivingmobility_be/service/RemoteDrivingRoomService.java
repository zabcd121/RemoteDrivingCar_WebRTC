package com.mobility.remotedrivingmobility_be.service;

import com.mobility.remotedrivingmobility_be.domain.member.Client;
import com.mobility.remotedrivingmobility_be.domain.remotedrivingroom.RemoteDrivingRoom;
import com.mobility.remotedrivingmobility_be.repository.ClientRepository;
import com.mobility.remotedrivingmobility_be.repository.MemberRepository;
import com.mobility.remotedrivingmobility_be.util.Parser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

import static com.mobility.remotedrivingmobility_be.domain.member.Client.*;

@Service
@RequiredArgsConstructor
public class RemoteDrivingRoomService {
    private final Parser parser;
    // repository substitution since this is a very simple realization
    private final Set<RemoteDrivingRoom> rooms = new TreeSet<>(Comparator.comparing(RemoteDrivingRoom::getId));
    private final MemberRepository memberRepository;
    private final ClientRepository clientRepository;

//    @Autowired
//    public RemoteDrivingRoomService(final Parser parser) {
//        this.parser = parser;
//    }

    public Set<RemoteDrivingRoom> getRooms() {
        final TreeSet<RemoteDrivingRoom> defensiveCopy = new TreeSet<>(Comparator.comparing(RemoteDrivingRoom::getId));
        defensiveCopy.addAll(rooms);

        return defensiveCopy;
    }

    public Boolean addRoom(final RemoteDrivingRoom room) {
        return rooms.add(room);
    }

    public Optional<RemoteDrivingRoom> findRoomByStringId(final String sid) {
        // simple get() because of parser errors handling
        return rooms.stream().filter(r -> r.getId().equals(parser.parseId(sid).get())).findAny();
    }

    public Long getRoomId(RemoteDrivingRoom room) {
        return room.getId();
    }

    public Set<Client> getClients(final RemoteDrivingRoom room) {
        return Optional.ofNullable(room)
                .map(r -> Collections.unmodifiableSet(r.getClients()))
                .orElse(Collections.emptySet());
    }

    public void join(Long memberId, RemoteDrivingRoom room, WebSocketSession session) {
        room.join(
                createClient(
                        memberRepository.getReferenceById(memberId),
                        room,
                        session.getId())
        );
    }

    public void removeClientBySessionId(String sessionId) {
        clientRepository.findBySessionId(sessionId).ifPresent(c -> {
            c.getRemoteDrivingRoom().leave(c);
            clientRepository.delete(c);
        });
    }
}
