package com.mobility.remotedrivingmobility_be.domain.member;

import com.mobility.remotedrivingmobility_be.domain.remotedrivingroom.RemoteDrivingRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Client {
    @Id @GeneratedValue
    @Column(name = "CLIENT_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "REMOTE_DRIVING_ROOM_ID")
    private RemoteDrivingRoom remoteDrivingRoom;

    private String sessionId; // WebSocketSession -> String

    public static Client createClient(Member member, RemoteDrivingRoom remoteDrivingRoom, String sessionId) {
        Client client = new Client();
        client.member = member;
        client.remoteDrivingRoom = remoteDrivingRoom;
        client.sessionId = sessionId;
        return client;
    }

//    public void changeRemoteDrivingRoom(RemoteDrivingRoom remoteDrivingRoom) {
//        if(this.remoteDrivingRoom != null) {
//            this.remoteDrivingRoom.getClients().remove(this);
//        }
//        this.remoteDrivingRoom = remoteDrivingRoom;
//        remoteDrivingRoom.getClients().add(this);
//    }

    public void setRemoteDrivingRoom(RemoteDrivingRoom remoteDrivingRoom) {
        this.remoteDrivingRoom = remoteDrivingRoom;
    }

}
