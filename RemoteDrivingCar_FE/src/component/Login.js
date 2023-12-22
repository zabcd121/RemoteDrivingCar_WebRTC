import React, {useEffect, useRef, useState} from 'react';
import '../styles/login.css';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import Header from "./Header";
import box_photo from "../images/box_photo.png";

const Login = (props) => {
    const [id, setID] = useState('');
    const [pw, setPW] = useState('');

    const idRef = useRef(null);

    const navigator = useNavigate();

    useEffect(() => {
        if (idRef.current !== null)
            idRef.current.focus();
    }, [])

    const changeId = e => {
        setID(e.target.value)
    }

    const changePw = e => {
        setPW(e.target.value)
    }

    const LoginReq = e => {
        e.preventDefault();

        axios.post(`http://localhost:8030/api/members/login`, {
            loginId: id,
            password: pw
        })
        .then(result => {
            const memberInfo = result.data.result.memberInfo;
            sessionStorage.setItem('memberId', memberInfo.memberId);
            sessionStorage.setItem('loginId', memberInfo.loginId)

            const tokenInfo = result.data.result.tokenInfo;
            sessionStorage.setItem('accessToken', tokenInfo.accessToken);
            sessionStorage.setItem('refreshToken', tokenInfo.refreshToken);
            navigator('/enrollment');
        })
        .catch(err => {
        });
        setID('');
        setPW('');
    }

    return (
        <>
            <Header props/>
        <div id = 'car' type='image'>
            <img loading="lazy" className='pic' alt="this is car" src = {box_photo}></img>
        </div>
            <form className="loginBox">
                <h2>Login to Your Account</h2>
                <h4 id ='goTo'><Link to="/register">Go to Signup</Link></h4>
                <h4 id='idTag'>Id</h4>
                <p><input type="text" ref={idRef} required value={id} onChange={changeId}></input></p>
                <h4 id='pwTag'>Password</h4>
                <p><input type="password" required value={pw} onChange={changePw}></input></p>

                <br></br>


                <button type="submit" onClick={LoginReq}>Login</button>
            </form>
        </>
    );
};

export default Login;