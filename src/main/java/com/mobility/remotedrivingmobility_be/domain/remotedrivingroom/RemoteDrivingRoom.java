package com.mobility.remotedrivingmobility_be.domain.remotedrivingroom;

import com.mobility.remotedrivingmobility_be.domain.car.Car;
import com.mobility.remotedrivingmobility_be.domain.member.Client;
import lombok.*;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.CascadeType.*;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class RemoteDrivingRoom {

    @Id @GeneratedValue
    @Column(name = "REMOTE_DRIVING_ROOM_ID")
    private Long id;

    // sockets by user names
//    private Map<String, WebSocketSession> clients = new HashMap<>();
//    private Set<WebSocketSession> sessions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CAR_ID")
    private Car car;

    @OneToOne(mappedBy = "remoteDrivingRoom", fetch = FetchType.LAZY, cascade = ALL)
    private Client client;

    private String name;

    //이건 빼야될듯
    public static RemoteDrivingRoom createRemoteDrivingRoom(Car attemptingCar) {
        RemoteDrivingRoom room = new RemoteDrivingRoom();
        room.car = attemptingCar;
        room.name = attemptingCar.getNumber();
        return room;
    }

    public void join(Client newClient) {
        changeClient(newClient);
    }

    public void leave(Client leavingClient) {
        removeClient(leavingClient);
    }

    private void changeClient(Client newClient) {
        this.client = newClient;
        if (newClient.getRemoteDrivingRoom() != this) {
            newClient.setRemoteDrivingRoom(this);
        }
    }

    private void removeClient(Client leavingClient) {
        this.client.setRemoteDrivingRoom(null);
        this.client = null;
    }

}
