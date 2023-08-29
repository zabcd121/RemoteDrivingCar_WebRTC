//connecting to our signaling server
//우리가 구축 한 Spring Boot 시그널링 서버가 http : // localhost : 8080 에서 실행되고 있음
const address = 'wss://remotedriving.cloud/ws';
//const address = 'ws://127.0.0.1:55611/ws'
var conn = new WebSocket(address);
var input = document.getElementById("messageInput");
var videoElement = document.getElementById('videoElement');
var videoElement2 = document.getElementById('videoElement2');
var videoElement3 = document.getElementById('videoElement3');
/**
 * 브라우저에서 미디어 스트림을 가져오기
 * WebRTC는 이를위한 API를 제공
 */
const constraints = {
    video: true, audio : true
};

let streams;

// navigator.mediaDevices.getUserMedia(constraints).
// then(function(stream) { /* use the stream */ })
//     .catch(function(err) { /* handle the error */});



/**
 * constraints 객체를 사용하여 비디오의 프레임 속도, 너비 및 높이를 지정
 */


conn.onopen = function() {
    console.log("Connected to the signaling server");
    initialize();

    console.log('send Join');
    sendJoin();
};

conn.addEventListener("open", () => {
    console.log("Session is open");
})


conn.addEventListener("message", (event) => {
    console.log("Message from Server ", event)
})

conn.onclose = function () {
    conn = new WebSocket(address);
    console.log('reconnected');
}

conn.onmessage = function(msg) {
    console.log("Got message", msg.data);
    var content = JSON.parse(msg.data);
    switch (content.type) {
        // when somebody wants to call us
        case "offer":
            handleOffer(content);
            break;
        case "answer":
            handleAnswer(content);
            break;
        // when a remote peer sends an ice candidate to us
        case "ice_car":
            handleCandidate(content);
            break;
        default:
            break;
    }
};

/**
 * signaling server로 메세지 보내기 위해 send 메소드 설정
 */

function isOpen(ws) {
    return ws.readyState === ws.OPEN
}
function send(message) {
    if(!isOpen(conn)) return;
    conn.send(JSON.stringify(message));
}

function sendJoin() {
    send({
        "from": "4", //실제 memberId Long을 문자열로 넣으면 됨
        "type": "join",
        "data": "10", //실제 roomId Long을 문자열로 넣으면 됨
        "candidate": null,
        "sdp": null
    })
}

var peerConnection;
var dataChannel;
//var input = document.getElementById("messageInput");



function initialize() {
    var configuration = {
        "iceServers" : [ {
            "url" : "stun:stun.l.google.com:19302"
        } ]
    };

    /**
     * 간단한 RTCDataChannel 설정
     * 여기서는 stun만 설정
     */
    peerConnection = new RTCPeerConnection(configuration);

    /**
     * WebRTC는 ICE (Interactive Connection Establishment) 프로토콜을 사용하여 피어를 검색하고 연결을 설정
     * peerConnection 에 로컬 설명을 설정하면 icecandidate 이벤트가 트리거된다
     * remote Peer가 Set of remote candidates에 Candidate를 추가 할 수 있도록 candidate를 remote peer로 전송
     * 이를 위해 onicecandidate 이벤트에 대한 리스너를 만든다.
     *
     * ICE candidate의 모든 candidate가 수집 될 때 이벤트는 빈 후보 문자열을 다시 트리거
     * 그 이유는 빈 문자열을 remote peer에게 전달하여 모든 icecandidate 객체가 수집 되었음을 알리기 위해
     */
    // Setup ice handling

    let count = 0;
    peerConnection.ontrack = e => {
        console.log("여기 들어옴")
        console.log(e);
        if(count == 0) {
            videoElement.srcObject = e.streams[0];
        }else if(count == 1) {
            videoElement2.srcObject = e.streams[0];
        }else if(count ==2) {
            videoElement3.srcObject = e.streams[0];
        }
        ++count;
        
        //
    }

    peerConnection.onicecandidate = function(event) {
        console.log(event);
        
        if (event.candidate) {
            send({
                "from": "4", //memberId Long을 문자열로 넣어줘야함 
                "type": "ice_client",
                "data": "",
                "candidate": event.candidate,
                "sdp": null
            })
        }
       
    };

    peerConnection.onconnectionstatechange = function(event) {
        switch(peerConnection.connectionState) {
          case "connected":
            console.log("connect")
            break;
          case "disconnected":
            console.log("disconnected")
            break;
          case "failed":
            console.log("failed")
            // One or more transports has terminated unexpectedly or in an error
            break;
          case "closed":
            // The connection has been closed
            console.log("closed")
            break;
        }
      }

    /**
     * 메시지 전달에 사용할 dataChannel
     */
    // creating data channel
    dataChannel = peerConnection.createDataChannel("dataChannel");

    dataChannel.onopen = () => {
        console.log("dataChannel open");
    }

    

    /**
     * 데이터 채널의 다양한 이벤트에 대한 리스너
     */
    dataChannel.onerror = function(error) {
        console.log("Error occured on datachannel:", error);
    };

    dataChannel.onclose = function() {
        console.log("data channel is closed");
    };

    dataChannel.addEventListener("open", event => {
        console.log(event);
    })

    /**
     * 다른 피어에서 메시지를 수신하기 위해 onmessage 이벤트에 대한 리스너를 생성
     */
    // when we receive a message from the other peer, printing it on the console
    dataChannel.onmessage = function(event) {
        console.log("message:", event.data);
    };

    /**
     * 데이터 채널에서 메시지를 수신하기위해 peerConnection 객체 에 콜백을 추가
     */
    peerConnection.ondatachannel = function (event) {
        console.log(event);
        dataChannel = event.channel;
    };
    
    

    //peerConnection.onaddstream = handleRemoteStreamAdded;



    /**
     * WebRTC WebRTC peerconnection object에 스트림을 추가
     * peerconnection에 스트림을 추가하면 연결된 피어 에서 addstream 이벤트가 트리거
     */
 //   peerConnection.addStream(stream);

    /**
     * remote peer 에서 listener를 통해 스트림을 수신
     * 해당 스트림은  HTML 비디오 요소로 설정
     */
    // peerConnection.onaddstream = function(event) {
    //     videoElement.srcObject = event.stream;
    // };

}

/**
 * 1. 오퍼를 생성하고 이를 peerConnection 의 로컬 설명으로 설정
 * 2. 이후 offer 을 다른 peer 에게 보낸다.
 */
function createOffer() {
    
}

/**
 * offer를 받은 peer는 이를 remotedescription으로 설정하고
 * answer를 생성하여 처음 peer 에게 보낸다.
 */
function handleOffer(offer) {
    console.log(offer);
    peerConnection.setRemoteDescription(new RTCSessionDescription(offer.sdp));
    
    peerConnection.createAnswer(function(answer) {
        
        peerConnection.setLocalDescription(answer);
        console.log(answer);
        console.log('send answer');
        send({
            "from": "4", //memberId Long을 문자열로 넣어줘야함 
            "type": "answer",
            "data": "",
            "candidate": null,
            "sdp": answer
        });
        
    }, function(error) {
        alert("Error creating an answer");
    });
    
    
};

/**
 * 다른 peer가 보낸 ICE candidate를 처리해야 하는데
 * 이 candidate를 받은 remote Peer는 해당 candidate를 candidate pool의 추가
 */
function handleCandidate(message) {
    console.log(message);
    peerConnection.addIceCandidate(new RTCIceCandidate(message.candidate));
    console.log(new RTCIceCandidate(message.candidate));
};

/**
 * 처음 Peer는 anwser를 받고 setRemoteDescription 으로 설정
 */
function handleAnswer(answer) {
    peerConnection.setRemoteDescription(new RTCSessionDescription(answer));
    console.log("connection established successfully!!");
};

/**
 * 연결 되었으므로 dataChannel 의 send 메서드를 사용하여 피어간에 메시지를 보낼 수 있다.
 */

function sendMessage() {
    dataChannel.send(input.value);
    input.value = "";
}