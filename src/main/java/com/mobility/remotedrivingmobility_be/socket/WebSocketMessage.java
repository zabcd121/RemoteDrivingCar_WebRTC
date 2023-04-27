package com.mobility.remotedrivingmobility_be.socket;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode
public class WebSocketMessage {
    private String from;
    private String type;
    private String data;
    private Object candidate;
    private Object sdp;

    @Override
    public String toString() {
        return "WebSocketMessage{" +
                "from='" + from + '\'' +
                ", type='" + type + '\'' +
                ", data='" + data + '\'' +
                ", candidate=" + candidate +
                ", sdp=" + sdp +
                '}';
    }
}