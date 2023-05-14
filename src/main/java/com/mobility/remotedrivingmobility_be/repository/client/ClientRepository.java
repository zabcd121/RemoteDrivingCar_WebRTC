package com.mobility.remotedrivingmobility_be.repository.client;

import com.mobility.remotedrivingmobility_be.domain.member.Client;
import com.mobility.remotedrivingmobility_be.domain.remotedrivingroom.RemoteDrivingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<List<Client>> findByRemoteDrivingRoom(RemoteDrivingRoom room);

    Optional<Client> findBySessionId(String sessionId);
}
