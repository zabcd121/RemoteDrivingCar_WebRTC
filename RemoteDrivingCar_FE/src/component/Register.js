import React, {useEffect, useRef, useState} from 'react';
import '../styles/register.css';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import Header from "./Header";

const Register = () => {
    
    const [id, setID] = useState('');
    const [pw, setPW] = useState('');

    const idRef = useRef(null);

    useEffect(() => {
        if (idRef.current !== null)
            idRef.current.focus();
    }, [])

    const navigator = useNavigate();

    const changeId = e => {
        setID(e.target.value);
    }

    const changePw = e => {
        setPW(e.target.value);
    }

    const registerBtn = useRef();

    const registerReq = e => {
        e.preventDefault();

        // axios.post(`https://remotedriving.cloud/api/members/signup`, {
        axios.post(`http://localhost:8030/api/members/signup`, {
            loginId: id,
            password: pw
        })
        .then(result => {
            navigator('/login');
        })
        .catch(err => {
            console.error(err);

            if (err.data.code === '0102') {
                alert('이미 존재하는 계정입니다.');
            }
        });
        setID('');
        setPW('');
    }


    return (
        <>
            <Header props/>
        <div id = 'car' type='image'>
            <img loading="lazy" className='pic' src = "https://c.wallhere.com/photos/f6/1b/car_automotive_HiPhi_electric_car-1981857.jpg!d"></img>
        </div>
            <form className="registerBox">
                <h2>Create your Account!</h2>
                <h4 id='idTag'>Id</h4>
                <p><input type="text" ref={idRef} required value={id} onChange={changeId}></input></p>
                <h4 id='pwTag'>Password</h4>
                <p><input type="password" required value={pw} onChange={changePw}></input></p>

                <br></br>


                <button type="submit" ref={registerBtn} onClick={registerReq}>Singup</button>
            </form>
        </>
    );
};

export default Register;