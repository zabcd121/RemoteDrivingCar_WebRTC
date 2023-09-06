var conn = new WebSocket('wss://remotedriving.cloud/ws')
//var conn = new WebSocket('ws://127.0.0.1:55611/ws')
let carId = "7";
let memberId = "4";
let roomId = "10";


let localVideo = document.getElementById('local');
let localVideo2 = document.getElementById('local2');
let localVideo3 = document.getElementById('local3');
var constraints = {
    // video : {
    //     frameRate : {
    //         ideal : 10,
    //         max : 15
    //     },
    //     width : 1280,
    //     height : 720,
    //     facingMode : "user"
    // }
    video: true, audio: false
};

const deviceIds = [];

const streams = new MediaStream();

navigator.mediaDevices.enumerateDevices().then(device => {
    device.filter(atomDe => atomDe.kind === 'videoinput').forEach(de => deviceIds.push(de))
    console.log(deviceIds)

    navigator.mediaDevices.getUserMedia({
        audio: false,
        video: {
            deviceId: `${deviceIds[0].deviceId}`
        }
    })
    .then(stream => {
        localVideo.srcObject = stream;
        const videoTrack = stream.getVideoTracks()[0];
        streams.addTrack(videoTrack, stream);
        peerConnection.addTrack(videoTrack, stream)
        console.log("localvideo1 set completed")
    }).catch(err => console.log(err));
    
    navigator.mediaDevices.getUserMedia({
        audio: false,
        video: {
            deviceId: `${deviceIds[1].deviceId}`
        }
    }).then(stream => {
        localVideo2.srcObject = stream;
        
        const videoTrack = stream.getVideoTracks()[0];
        streams.addTrack(videoTrack, stream);
        peerConnection.addTrack(videoTrack, stream);
        console.log("localvideo2 set completed")
    }).catch(err => console.log(err));

    navigator.mediaDevices.getUserMedia({
        audio: false,
        video: {
            deviceId: `${deviceIds[2].deviceId}`
        }
    }).then(stream => {
        localVideo3.srcObject = stream;
        
        const videoTrack = stream.getVideoTracks()[0];
        streams.addTrack(videoTrack, stream);
        peerConnection.addTrack(videoTrack, stream);
        console.log("localvideo3 set completed")
    }).catch(err => console.log(err));
})

let myOffer;

function send(message) {
	console.log('send : ' + JSON.stringify(message));
	conn.send(JSON.stringify(message));
}

var peerConnection;
var dataChannel;

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
console.log('RTCPeerConnection 객체 생성');


var input = document.getElementById("messageInput");

function initialize() {
    
    

    // peerConnection.onaddtrack = () => {
    //     console.log('hello')
    // }
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
    peerConnection.onicecandidate = function(event) {
		
        // console.log('<event>', event)
        // console.log('<event.candidate>', event.candidate)
        
        console.log('candidate 수신함')
        console.log(event.candidate)

        if (event.candidate) {
            console.log('candidate 전송함')

            const candidateData = {
                "from": carId,
                "type": "ice_car",
                "data": null,
                "candidate": event.candidate,
                "sdp": null
            }

            send(candidateData);
        }

        else {
            console.log('candidate 수집 완료되었습니다')
        }
    };
	console.log('Candidate 콜백 등록')

    /**
     * 메시지 전달에 사용할 dataChannel
     */
    // creating data channel
    dataChannel = peerConnection.createDataChannel("dataChannel", {
        reliable : true
    });
    console.log('Data Channel', dataChannel);

    /**
     * 데이터 채널의 다양한 이벤트에 대한 리스너
     */
    dataChannel.onerror = function(error) {
        console.log("Error occured on datachannel:", error);
    };

    dataChannel.onclose = function() {
        console.log("data channel is closed");
    };

    dataChannel.onopen = () => {
        console.log("dataChannel open");
    }

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
        dataChannel = event.channel;
    };
	console.log('Data Channel 콜백 등록')
    

    /**
     * WebRTC WebRTC peerconnection object에 스트림을 추가
     * peerconnection에 스트림을 추가하면 연결된 피어 에서 addstream 이벤트가 트리거
     */

    /**
     * remote peer 에서 listener를 통해 스트림을 수신
     * 해당 스트림은  HTML 비디오 요소로 설정
     */
    // peerConnection.onaddstream = function(event) {
    //     videoElement.srcObject = event.stream;
    // };

}


//이 위로 건들거 없음.

function createOffer() {

	console.log('create offer 호출')

    peerConnection.createOffer(function(offer) {
		
        console.log('나의 OFFER 생성하였음 : ' + offer)
        peerConnection.setLocalDescription(offer);
        myOffer = offer;

        console.log('myOffer', offer)

        const offerData = {
            "from": carId,
            "type": "offer",
            "data": roomId,
            "candidate": null,
            "sdp": offer
        }

        console.log('3. OFFER 전달')
        send(offerData);
        console.log('OFFER 전달 완료')

    }, function(error) {
        alert("Error creating an offer");
    });

    // then 쓰는거로 바꾸자
}

function sendWait() {

	const waitData = {
		"from": carId,
		"type": "wait",
		"data": null,
		"candidate": null,
		"sdp": null
	}

	console.log('1. WAIT 전달')
	send(waitData);//1. wait

}

function sendOffer() {

    // console.log('candidate 수집 대기 시작')
    // while(isCollectFinished == false);  
    // candidate 수신이 완료될 때 까지 대기
    // console.log('candidate 수집 완료')

    console.log('myOffer', myOffer)

    const offerData = {
        "from": carId,
        "type": "offer",
        "data": roomId,
        "candidate": null,
        "sdp": myOffer
    }

    console.log('3. OFFER 전달')
    send(offerData);
    console.log('OFFER 전달 완료')
}

conn.onopen = () => {

    console.log('Car Browser')
	initialize();
    sendWait();
}

function receiveAnswer(answer) {

    const answer_sdp = JSON.parse(answer).sdp;
    console.log('remote description 저장', answer_sdp);
    peerConnection.setRemoteDescription(new RTCSessionDescription(answer_sdp));

}

function addCandidate(message) {

    const candidate = JSON.parse(message).candidate;

    peerConnection.addIceCandidate(
        new RTCIceCandidate(candidate)
    );
    console.log('candidate 추가완료', candidate);

    showPeerConnection();

}

function showPeerConnection() {
    console.log('@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@')
    console.log('PeerConnectino', peerConnection);
    console.log('iceGatheringState : ', peerConnection.iceGatheringState);
    console.log('local sdp : ', peerConnection.localDescription);
    console.log('remote sdp : ', peerConnection.remoteDescription);
    console.log('@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@')
}

conn.onmessage = (message) => {
	console.log('===============MESSAGE HANDLE==============')

	const msg = JSON.stringify(message.data)
	const type = JSON.parse(message.data).type;

	console.log('TYPE : ' + type)
	console.log('MSG : ' + msg)

	switch(type) {
		case "join":
			console.log('2. JOIN MESSAGE 수신')
			
            createOffer();

			console.log('JOIN MESSAGE 핸들 완료')
			break;
		
		case "answer":
			console.log('4. ANSWER 수신')
            receiveAnswer(message.data);
			console.log('ANSWER 핸들링 완료')
            //peerConnection.addStream(streams);
			break;

		case "sessionConnected":
			console.log('Session Established')
			break;

        case "ice_client":
            console.log('client candidate 수신함')
            addCandidate(message.data);
            break;

		default:
			// TODO : 세션 연결 확인 출력 case 만들기
			console.log('default switch');
			break;
	}

	console.log('===========================================')
}

function openDataChannel() {
    console.log('dataChannel', dataChannel);
}

function sendMessage() {
    dataChannel.send(input.value);
    input.value = "";
}

