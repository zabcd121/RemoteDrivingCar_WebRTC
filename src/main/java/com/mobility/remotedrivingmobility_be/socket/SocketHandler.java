package com.mobility.remotedrivingmobility_be.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobility.remotedrivingmobility_be.domain.member.Client;
import com.mobility.remotedrivingmobility_be.domain.remotedrivingroom.RemoteDrivingRoom;
import com.mobility.remotedrivingmobility_be.service.client.ClientService;
import com.mobility.remotedrivingmobility_be.service.remotedrivingroom.RemoteDrivingRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

import static com.mobility.remotedrivingmobility_be.socket.MessageType.*;
import static com.mobility.remotedrivingmobility_be.socket.MessageType.MSG_TYPE_TEXT;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketHandler extends TextWebSocketHandler {

    private final RemoteDrivingRoomService roomService;
    private final ClientService clientService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, WebSocketSession> sessionIdMap = new HashMap<>();
    private Map<String, WebSocketSession> carSessionMap = new HashMap<>();

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) {
        sendMessage(session, new WebSocketMessage("Server", MSG_TYPE_JOIN, Boolean.toString(sessionIdMap.containsKey(session.getId())), null, null));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        try {
            WebSocketMessage message = objectMapper.readValue(textMessage.getPayload(), WebSocketMessage.class);
            log.debug("[ws] Message of {} type from {} received", message.getType(), message.getFrom());

            switch (message.getType()) {
                case MSG_TYPE_TEXT:
                    log.debug("[ws] Text message: {}", message.getData());
                    break;

                case MSG_TYPE_WAIT:
                    carSessionMap.put(message.getFrom(), session);
                    break;

                case MSG_TYPE_OFFER:
                    offerProcess(message, session);
                    break;

                case MSG_TYPE_ANSWER:
                    answerProcess(message, session);
                    break;

                case MSG_TYPE_JOIN:
                    log.debug("[ws] {} has joined Room: #{}", message.getFrom(), message.getData());

                    RemoteDrivingRoom room = roomService.joinRoom(Long.parseLong(message.getFrom()), Long.parseLong(message.getData()), session);
                    sessionIdMap.put(session.getId(), session);
                    sendMessage(carSessionMap.get(room.getCar().getId().toString()),
                            new WebSocketMessage(
                                    message.getFrom(),
                                    MSG_TYPE_JOIN,
                                    Boolean.toString(sessionIdMap.containsKey(session.getId())),
                                    null,
                                    null));
                    break;

                case MSG_TYPE_LEAVE:
                    log.debug("[ws] {} is going to leave Room: #{}", message.getFrom(), message.getData());
                    roomService.leaveRoom(session.getId());
                    break;

                default:
                    log.debug("[ws] Type of the received message {} is undefined!", message.getType());
            }

        } catch (IOException e) {
            log.debug("An error occured: {}", e.getMessage());
        }
    }

    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) {
        log.debug("[ws] Session has been closed with status {}", status);
        sessionIdMap.remove(session.getId());
    }

    private void offerProcess(WebSocketMessage message, WebSocketSession session) { // RTCPeerConnection에 관한 candidate와 SDP(offer)를 생성한 후 서버에 넘겨줘 저장시킨다
        Object candidate = message.getCandidate();
        Object sdp = message.getSdp();
        log.debug("[ws] Signal: {}",
                candidate != null
                        ? candidate.toString().substring(0, 64)
                        : sdp.toString().substring(0, 64));

        Client oppositeClient = clientService.searchClientBySessionId(session.getId());
        if (oppositeClient != null) {
            RemoteDrivingRoom joinedRoom = oppositeClient.getRemoteDrivingRoom();
            sendMessage(
                    sessionIdMap.get(oppositeClient.getSessionId()),
                    new WebSocketMessage(
                        joinedRoom.getId().toString(),
                        message.getType(),
                        message.getData(),
                        candidate,
                        sdp));
        }
    }

    private void answerProcess(WebSocketMessage message, WebSocketSession session) { // RTCPeerConnection에 관한 candidate와 SDP(offer)를 생성한 후 서버에 넘겨줘 저장시킨다
        Object candidate = message.getCandidate();
        Object sdp = message.getSdp();
        log.debug("[ws] Signal: {}",
                candidate != null
                        ? candidate.toString().substring(0, 64)
                        : sdp.toString().substring(0, 64));

        Client client = clientService.searchClientBySessionId(session.getId());
        if (client != null) {
            RemoteDrivingRoom joinedRoom = client.getRemoteDrivingRoom();
            sendMessage(
                    carSessionMap.get(joinedRoom.getCar().getId().toString()),
                    new WebSocketMessage(
                            message.getFrom(),
                            message.getType(),
                            message.getData(),
                            candidate,
                            sdp));
        }
    }

    private void sendMessage(WebSocketSession session, WebSocketMessage message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            log.debug("An error occured: {}", e.getMessage());
        }
    }

    /*Object candidate = message.getCandidate();
    Object sdp = message.getSdp();
                    log.debug("[ws] Signal: {}",
    candidate != null
            ? candidate.toString().substring(0, 64)
                                    : sdp.toString().substring(0, 64));

    RemoteDrivingRoom rm = clientService.searchClientBySessionId(session.getId()).getRemoteDrivingRoom();
    //RemoteDrivingRoom rm = sessionIdToRoomMap.get(session.getId());
                    if (rm != null) {
        //Map<String, WebSocketSession> clients = roomService.getClients(rm);
        Set<Client> clients = roomService.getClients(rm);
        for(Client client : clients)  {
            // send messages to all clients except current user
            if (!client.getMember().getName().equals(memberId)) {
                // select the same type to resend signal
                sendMessage(sessionIdMap.get(client.getSessionId()),
                        new WebSocketMessage(
                                memberId,
                                message.getType(),
                                data,
                                candidate,
                                sdp));
            }
        }
    }
                    break;*/

    /*
    // remove the client which leaves from the Room clients list
//                    Optional<String> client = roomService.getClients(room).stream()
//                            .filter(c -> Objects.equals(c.getSessionId(), session.getId()))
//                            .map(Map.Entry::getKey)
//                            .findAny();
     */
}
