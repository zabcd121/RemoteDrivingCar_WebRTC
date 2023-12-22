import React, {useEffect, useRef, useState} from 'react';
import '../styles/carList.css';
import axios from 'axios';
import {useNavigate} from "react-router-dom";
import Header from "./Header";

const CarList = () => {
    const [carList, setCarList] = useState([]);

    const ulRef = useRef();

    const navigator = useNavigate();



    useEffect(() => {
        axios.get("http://localhost:8030/api/cars", {
            headers: {
                Authorization : sessionStorage.getItem('accessToken')
            }
        })
        .then(result => {
            setCarList(result.data.result);
            console.log(result.data.result)
        }).catch(err => {
            let errorCode = err.response.data.code;
            if (errorCode === '0210') {
                axios.post("http://localhost:8030/api/members/token/reissue", {

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
                })
                    .then(() => {
                        axios.get("http://localhost:8030/api/cars", {
                            headers: {
                                Authorization : sessionStorage.getItem('accessToken')
                            }
                        })
                            .then(result => {
                                setCarList(result.data.result);
                                console.log(result.data.result)
                            })
                    })
            }
            console.log(err);
        })
    }, []);

    const goStreaming = (carId) => {
        if(carId === 0)
            return;
        else {
            navigator(`/streaming/${carId}`)
        }
    }

    const goEnrollment = () => {
        navigator(`/enrollment`);
    }

    return (
        <>
            <Header props/>
            <div id="nav_section">
                <div id ="navi">
                    <div className="letter_space"><p onClick={() => goEnrollment()}>AGV 등록</p></div>

                    <div className="letter_space"><p>AGV 리스트</p></div>
                </div>
            </div>
            
            <div id='listSection'>
                <ul ref={ulRef} id="carList">{
                    carList.map(car => <li key={car.carId}>{car.carNumber}<br />
                    <p onClick={() => goStreaming(car.carId)}>접속하기</p></li>)
                    // TODO 정렬 flexbox 해보기
                }</ul>
            </div>
        </>
    );
};

export default CarList;