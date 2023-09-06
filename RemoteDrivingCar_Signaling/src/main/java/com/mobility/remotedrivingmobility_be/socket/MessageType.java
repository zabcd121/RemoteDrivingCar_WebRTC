package com.mobility.remotedrivingmobility_be.socket;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MessageType {
    public static final String MSG_TYPE_SESSION_CONNECTED = "sessionConnected";
    public static final String MSG_TYPE_JOIN = "join";
    public static final String MSG_TYPE_LEAVE = "leave";
    public static final String MSG_TYPE_TEXT = "text";
    public static final String MSG_TYPE_OFFER = "offer";
    public static final String MSG_TYPE_ANSWER = "answer";
    public static final String MSG_TYPE_ICE_CAR = "ice_car";
    public static final String MSG_TYPE_ICE_CLIENT = "ice_client";
    public static final String MSG_TYPE_WAIT = "wait";
//    JOIN("join"),
//    LEAVE("leave"),
//    TEXT("text"),
//    OFFER("offer"),
//    ANSWER("answer"),
//    ICE("ice");
//
//    private final String type;

}
