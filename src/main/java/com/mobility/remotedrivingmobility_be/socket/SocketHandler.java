package com.mobility.remotedrivingmobility_be.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobility.remotedrivingmobility_be.domain.member.Client;
import com.mobility.remotedrivingmobility_be.domain.remotedrivingroom.RemoteDrivingRoom;
import com.mobility.remotedrivingmobility_be.service.ClientService;
import com.mobility.remotedrivingmobility_be.service.RemoteDrivingRoomService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

import static com.mobility.remotedrivingmobility_be.socket.MessageType.*;
import static com.mobility.remotedrivingmobility_be.socket.MessageType.MSG_TYPE_TEXT;


@Component
@RequiredArgsConstructor
public class SocketHandler extends TextWebSocketHandler {

    private final RemoteDrivingRoomService roomService;
    private final ClientService clientService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper = new ObjectMapper();

    // session id to room mapping
//    private Map<String, RemoteDrivingRoom> sessionIdToRoomMap = new HashMap<>();

    //sessionId, Session
    private Map<String, WebSocketSession> sessionIdMap = new HashMap<>();


//    private static final String MSG_TYPE_TEXT = "text";
//    private static final String MSG_TYPE_OFFER = "offer";
//    private static final String MSG_TYPE_ANSWER = "answer";
//    private static final String MSG_TYPE_ICE = "ice";


    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) {
        logger.debug("[ws] Session has been closed with status {}", status);
        sessionIdMap.remove(session.getId());
    }

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) {
        // webSocket has been opened, send a message to the client
        // when data field contains 'true' value, the client starts negotiating
        // to establish peer-to-peer connection, otherwise they wait for a counterpart
        sendMessage(session, new WebSocketMessage("Server", MSG_TYPE_JOIN, Boolean.toString(!sessionIdMap.isEmpty()), null, null));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        try {
            WebSocketMessage message = objectMapper.readValue(textMessage.getPayload(), WebSocketMessage.class);
            logger.debug("[ws] Message of {} type from {} received", message.getType(), message.getFrom());
            String memberId = message.getFrom(); // origin of the message
            String data = message.getData(); // payload

            RemoteDrivingRoom room;
            switch (message.getType()) {
                // text message from client has been received
                case MSG_TYPE_TEXT:
                    logger.debug("[ws] Text message: {}", message.getData());
                    // message.data is the text sent by client
                    // process text message if needed
                    break;

                // process signal received from client
                case MSG_TYPE_OFFER:
                case MSG_TYPE_ANSWER:
                case MSG_TYPE_ICE:
                    Object candidate = message.getCandidate();
                    Object sdp = message.getSdp();
                    logger.debug("[ws] Signal: {}",
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
                    break;

                // identify user and their opponent
                case MSG_TYPE_JOIN:
                    // message.data contains connected room id
                    logger.debug("[ws] {} has joined Room: #{}", memberId, message.getData());
                    room = roomService.findRoomByStringId(data)
                            .orElseThrow(() -> new IOException("Invalid room number received!"));
                    // add client to the Room clients list
                    roomService.join(Long.parseLong(memberId), room, session);
                    sessionIdMap.put(session.getId(), session);
                    //sessionIdToRoomMap.put(session.getId(), room);
                    break;

                case MSG_TYPE_LEAVE:
                    // message data contains connected room id
                    logger.debug("[ws] {} is going to leave Room: #{}", memberId, message.getData());
                    // room id taken by session id
                    room = sessionIdToRoomMap.get(session.getId());
                    // remove the client which leaves from the Room clients list
                    Optional<String> client = roomService.getClients(room).stream()
                            .filter(c -> Objects.equals(c.getSession(), session.getId()))
                            .map(Map.Entry::getKey)
                            .findAny();
                    client.ifPresent(c -> roomService.removeClientByName(room, c));
                    break;

                // something should be wrong with the received message, since it's type is unrecognizable
                default:
                    logger.debug("[ws] Type of the received message {} is undefined!", message.getType());
                    // handle this if needed
            }

        } catch (IOException e) {
            logger.debug("An error occured: {}", e.getMessage());
        }
    }

    private void sendMessage(WebSocketSession session, WebSocketMessage message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            logger.debug("An error occured: {}", e.getMessage());
        }
    }
}
