import React, {useEffect, useRef, useState} from 'react';
import {Link, useParams} from 'react-router-dom';
import '../styles/streaming.css';


const Streaming = () => {
    const address = 'ws://localhost:8040/ws';
    var conn = new WebSocket(address);
    //const {carId} = useParams();
    const carId = "7";
    //const memberId = sessionStorage.getItem('memberId');
    const memberId = "4";
    console.log(carId);
    const fixedSize = 50;
// arr[0] = seq X
    // arr[1] = steering
    // arr[2] = accel V
    // arr[3] = break V
    // arr[4] = JOY_X
    // arr[5] = JOY_Y
    // arr[6] = bit
    const [steering, setSteering] = useState(false);
    const [left, setLeft] = useState(false);
    const [right, setRight] = useState(false);
    const [gear, setGear] = useState('');
    const [accel, setAccel] = useState(0);
    const [breakP, setBreak] = useState(0);




    const inputRef = useRef({});

    const findObjRef = useRef({});
    const leftVideoRef = useRef({});
    const frontVideoRef = useRef({});
    const rightVideoRef = useRef({});

    const steeringRef = useRef(null);
    const leftRef = useRef(null);
    const rightRef = useRef(null);

    //connecting to our signaling server
//우리가 구축 한 Spring Boot 시그널링 서버가 http : // localhost : 8080 에서 실행되고 있음





    const constraints = {
        video: true, audio : true
    };

    let streams;

    // function base64ToBlob(base64) {
    //     const byteString = atob(base64);
    //     const buffer = new ArrayBuffer(byteString.length);
    //     const byteArray = new Uint8Array(buffer);
    //
    //     for (let i = 0; i < byteString.length; i++) {
    //         byteArray[i] = byteString.charCodeAt(i);
    //     }
    //
    //     return new Blob([buffer], {type: 'image/jpeg'});
    // }

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
            case "offer":
                handleOffer(content);
                break;
            case "answer":
                handleAnswer(content);
                break;
            case "ice_car":
                handleCandidate(content);
                break;
            default:
                break;
        }
    };


    function isOpen(ws) {
        return ws.readyState === ws.OPEN
    }
    function send(message) {
        if(!isOpen(conn)) return;
        conn.send(JSON.stringify(message));
    }

    function sendJoin() {
        send({
            "from": memberId,
            "type": "join",
            "data": carId,
            "candidate": null,
            "sdp": null
        })
    }

    var peerConnection;
    var dataChannel;



    function initialize() {
        var configuration = {
            "iceServers" : [ {
                "url" : "stun:stun.l.google.com:19302"
            } ]
        };
        peerConnection = new RTCPeerConnection(configuration);


        let count = 0;
        peerConnection.ontrack = e => {
            console.log("여기 들어옴")
            console.log(e);
            if(count === 0) {
                frontVideoRef.current.srcObject = e.streams[0];
            }else if(count === 1) {
                leftVideoRef.current.srcObject = e.streams[0];
            }else if (count === 2) {
                rightVideoRef.current.srcObject = e.streams[0];
            }
            ++count;

            //
        }

        peerConnection.onicecandidate = function(event) {
            console.log(event);

            if (event.candidate) {
                send({
                    "from": memberId,
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
                    break;
                case "closed":
                    console.log("closed")
                    break;
            }
        }

        dataChannel = peerConnection.createDataChannel("dataChannel");

        dataChannel.onopen = () => {
            console.log("dataChannel open");
            clickConnect();
        }



        dataChannel.onerror = function(error) {
            console.log("Error occured on datachannel:", error);
        };

        dataChannel.onclose = function() {
            console.log("data channel is closed");
        };

        dataChannel.addEventListener("open", event => {
            console.log(event);
        })


        // dataChannel.onmessage = function(event) {
        //     console.log("message:", event);
        //     //findObjRef.current.src = base64ToBlob(event.data);
        //     //const blob = event.data
        //     const blob = new Blob([event], {type:"image/jpeg"})
        //     console.log(blob)
        //     const imageUrl = window.URL.createObjectURL(blob)
        //     console.log('imageURL: ', imageUrl)
        //     const imgtag = document.getElementById("findObject")
        //     imgtag.src = imageUrl;
        //     //findObjRef.current.src = imageUrl
        // };

        peerConnection.ondatachannel = function (event) {
            console.log(event);
            dataChannel = event.channel;
        };


    }


    function createOffer() {

    }

    function handleOffer(offer) {
        console.log(offer);
        peerConnection.setRemoteDescription(new RTCSessionDescription(offer.sdp));

        peerConnection.createAnswer(function(answer) {

            peerConnection.setLocalDescription(answer);
            console.log(answer);
            console.log('send answer');
            send({
                "from": memberId,
                "type": "answer",
                "data": "",
                "candidate": null,
                "sdp": answer
            });

        }, function(error) {
            alert("Error creating an answer");
        });


    };


    function handleCandidate(message) {
        console.log(message);
        peerConnection.addIceCandidate(new RTCIceCandidate(message.candidate));
        console.log(new RTCIceCandidate(message.candidate));
    };

    function handleAnswer(answer) {
        peerConnection.setRemoteDescription(new RTCSessionDescription(answer));
        console.log("connection established successfully!!");
    };


    /**
     * 연결 되었으므로 dataChannel 의 send 메서드를 사용하여 피어간에 메시지를 보낼 수 있다.
     */

    const sendMessage = () => {
        dataChannel.send(inputRef.current.value);
        inputRef.current.value = "";
    }

    let port;
    let reader;
    let writer;
    let inputDone;
    let outputDone;
    let inputStream;
    let outputStream;

    const maxBytes = 7;

    async function connect() {
        // CODELAB: Add code to request & open port here.
        port = await navigator.serial.requestPort();
        // - Wait for the port to open.
        await port.open({ baudRate: 115200,
            //dataBits : maxBytes,
            stopBits: 1,
            parity: 'none',
            //bufferSize: maxBytes
        });
        // CODELAB: Add code setup the output stream here.
        // CODELAB: Send CTRL-C and turn off echo on REPL

        // CODELAB: Add code to read the stream here.
        console.log('port 잡힘');
        // eslint-disable-next-line no-undef
        let decoder = new TextDecoderStream();
        inputDone = port.readable.pipeTo(decoder.writable);
        inputStream = decoder.readable.pipeThrough(new TransformStream(new LineBreakTransformer()));


        //reader = port.readable.getReader();
        reader = inputStream.getReader();
        await readLoop();

    }

    async function clickConnect() {
        // CODELAB: Add disconnect code here.

        // CODELAB: Add connect code here.
        await connect();
        // CODELAB: Reset the grid on connect here.

        // CODELAB: Initialize micro:bit buttons.
    }

    const getGear = bit => {
        switch (bit) {
            case 0:
                return 'D';
            case 1:
                return 'R';
            // case 2:
            //     return 'R';
            // case 3:
            //     return 'P';
            default:
                return null;
        }
    }
    let left_arrow = document.getElementsByClassName("left_arrow")
    let right_arrow = document.getElementsByClassName("right_arrow")
    async function readLoop() {
        // CODELAB: Add read loop here.
        let arr = [];
        let cnt = 0;

        while (true) {
            const { value, done } = await reader.read();
            if (value) {
                //logs[cnt%7].textContent = value;
                //cnt++
                let arr = value.split(',');
                // let dataObject = new Data(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6], arr[7], arr[8], arr[9]);


                dataChannel.send(value);
                setSteering((arr[6] & 0b01000000))
                setLeft((arr[6] & 0b00100000))
                setRight((arr[6] & 0b00010000))

                if(arr[6] & 0b00001000) {
                    setLeft(true)
                    setRight(true)
                }

                console.log(left)
                if(left === true) {
                    left_arrow.src = process.env.PUBLIC_URL + '/images/left-arrow-abled2.png';
                } else if(left === false) {
                    left_arrow.src = process.env.PUBLIC_URL + '/images/left-arrow-disabled.png';
                }

                if(right === true) {
                    right_arrow.src = process.env.PUBLIC_URL + '/images/right-arrow-abled2.png';
                } else if(right === false) {
                    right_arrow.src = process.env.PUBLIC_URL + '/images/right-arrow-disabled.png';
                }
                setGear(getGear(arr[6] & 0b00000011))
                setAccel(arr[2])
                setBreak(arr[3])
            }

            if (done) {
                console.log('[readLoop] DONE', done);
                reader.releaseLock();
                break;
            }
        }
    }

    class LineBreakTransformer {
        constructor() {
            this.chunks = "";
        }

        transform(chunk, controller) {
            this.chunks += chunk;

            const lines = this.chunks.split("\r\n");
            this.chunks = lines.pop();
            lines.forEach((line) => controller.enqueue(line));
        }

        flush(controller) {
            controller.enqueue(this.chunks);
        }
    }

    return (
        <div className="streaming_container">
            <div className="upper">

                {/* <img id={"findObject"} ref={findObjRef} /> */}
                <Link id="leave" href="/list" to={"/list"}><p>연결 끊기</p></Link>
            </div>

            <div className="lower">
                <video id="left" className="streaming" autoPlay controls ref={leftVideoRef}>3</video>
                <video id="front" className="streaming" autoPlay controls ref={frontVideoRef}>4</video>
                <video id="right" className="streaming" autoPlay controls ref={rightVideoRef}>5</video>
            </div>
            <br />
            <span className="speedChecker">
                {/* <p ref={steeringRef}>{steering ? "상향등 켜짐" : ""}</p> */}
                <p>엑셀: {(Math.floor((accel-1000)/3000*100) - 4) < 0 ? 0 : Math.floor((accel-1000)/3000*100) - 4} %</p>
                {/* <p>브레이크: {(Math.floor((breakP-1000)/3000*100) - 4) < 0 ? 0 : Math.floor((breakP-1000)/3000*100) - 4} %</p> */}
                <p>기어 : {gear}</p>

                <img
                    src={left ? process.env.PUBLIC_URL + '/images/left-arrow-abled2.png' : process.env.PUBLIC_URL + '/images/left-arrow-disabled.png'}
                    alt="Left Blicker"
                    width={fixedSize}
                    height={fixedSize}
                    //placeholder="blur"
                    className="left_arrow"
                    ref={leftRef}
                />

                <img
                    src={right ? process.env.PUBLIC_URL + '/images/right-arrow-abled2.png' : process.env.PUBLIC_URL + '/images/right-arrow-disabled.png'}
                    alt="Right Blicker"
                    width={fixedSize}
                    height={fixedSize}
                    //placeholder="blur"
                    className="right_arrow"
                    ref={rightRef}
                />
            </span>

        </div>
    );
};

export default Streaming;