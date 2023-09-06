package com.mobility.remotedrivingmobility_be.repository.client;

import com.mobility.remotedrivingmobility_be.domain.member.Client;
import com.mobility.remotedrivingmobility_be.domain.remotedrivingroom.RemoteDrivingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("select c from Client c where c.remoteDrivingRoom.id = :roomId")
    Client findByRoomId(Long roomId);

    Optional<Client> findBySessionId(String sessionId);
}
