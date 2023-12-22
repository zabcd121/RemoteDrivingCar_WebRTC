import React, {useEffect, useRef, useState} from 'react';
import '../styles/enrollment.css';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Header from "./Header";

const Enrollment = () => {
    const inputRef = useRef(null);
    const navigator = useNavigate();
    const [carId, setCarID] = useState('');

    useEffect(() => {
        if(inputRef.current !== null)
            inputRef.current.focus();
    }, [])

    const inputCar = e => {
        setCarID(e.target.value);
    }

    const registerCarReq = e => {
        e.preventDefault();

        axios.post("http://localhost:8030/api/cars", {
            carNumber: carId
        }, {
           headers: {
                Authorization : sessionStorage.getItem('accessToken')
            }
        })
        .then(result => {
            setCarID('');
            navigator('/list');
    })
        .catch(err => {
            let errorCode = err.response.data.code;
            if (errorCode === '0210') {
                axios.post("http://192.168.16.227:8030/api/members/token/reissue", {

                }, {
                    headers: {
                        Authorization : sessionStorage.getItem('refreshToken')
                    }
                })
                .then(resp => {
                    sessionStorage.removeItem('accessToken');
                    sessionStorage.removeItem('refreshToken');
                    sessionStorage.setItem('accessToken', resp.data.result.accessToken);
                    sessionStorage.setItem('refreshToken', resp.data.result.refreshToken);

                    axios.post("https://remotedriving.cloud/api/cars", {
                        carNumber: carId
                }, {
                        headers: {
                        Authorization : sessionStorage.getItem('accessToken')
                    }
                }).then(() => {
                    setCarID('');
                    navigator('/list');
                }).catch(err => {
                    console.log(err);
                })
                })
                
            }
        }
            
            
        );
    }

    const goList = () => {
        navigator('/list');
    }
    return (
        <>
            <Header props/>
        <div id="nav_section">
            <div id ="navi">
                <div className="letter_space"><p onClick={() => goList()}>AGV 리스트</p></div>

                <div className="letter_space"><p>AGV 등록</p></div>
            </div>

            <div id="page_section">
                <div id="contents">
                    <p>차량 접속 번호</p>
                    <input type="text" ref={inputRef} placeholder="13수 1234" onChange={inputCar} value={carId}></input>
                    <br></br>
                    <button onClick={registerCarReq}>등록</button>
                    </div>
                
            </div>
        </div>
        </>

    );
};

export default Enrollment;