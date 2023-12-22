import React, { useEffect, useState } from 'react';
import '../styles/header.css';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const Header = (props) => {
    const [loginId, setLoginId] = useState();

    const navigator = useNavigate();

    const control = () => {
        if (sessionStorage.getItem('loginId') != null) {
            axios.delete("http://localhost:8030/api/members/logout", {
                headers: {
                    Authorization : sessionStorage.getItem('accessToken')
                }
            })
            .then(result => {
                sessionStorage.clear();
                navigator('/login');
        })
            .catch(err => {
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
                        axios.delete("http://localhost:8030/api/members/logout", {
                            headers: {
                                Authorization : sessionStorage.getItem('accessToken')
                            }
                        })
                            .then(result => {
                                sessionStorage.clear();
                                navigator('/login');
                            })
                    })

        }
    })
    }
        else {
            navigator('/login');
        }
    }

    return (
        <header>
            <div id="enroll_header">
                <section id="section1"><p>AGV 관리 시스템</p></section>
                <section id="section2"><p onClick={control}>{sessionStorage.getItem('loginId') === null ? 'login' : 'logout'}</p></section>
            </div>
        </header>
    );
};


export default Header;