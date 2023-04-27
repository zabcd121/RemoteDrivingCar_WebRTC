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

    @OneToMany(mappedBy = "remoteDrivingRoom", fetch = FetchType.LAZY, cascade = ALL)
    private Set<Client> clients = new HashSet<>();

    private String name;

    //이건 빼야될듯
    public static RemoteDrivingRoom createRemoteDrivingRoom(@NonNull String name) {
        RemoteDrivingRoom room = new RemoteDrivingRoom();
        room.name = name;
        return room;
    }

    public void join(Client newClient) {
        addClient(newClient);
    }

    public void leave(Client leavingClient) {
        removeClient(leavingClient);
    }

    private void addClient(Client newClient) {
        this.clients.add(newClient);
        if (newClient.getRemoteDrivingRoom() != this) {
            newClient.setRemoteDrivingRoom(this);
        }
    }

    private void removeClient(Client leavingClient) {
        for (Client client : this.clients) {
            if (client.getId() == leavingClient.getId()) {
                clients.remove(client);
                break;
            }
        }
    }

}
