import './App.css';
import Header from './component/Header';
import Login from './component/Login';
import {Routes, Route, useNavigate} from 'react-router-dom';
import Register from './component/Register';
import Enrollment from './component/Enrollment';
import CarList from './component/CarList';
import Streaming from './component/Streaming';
import {useEffect} from "react";

const App = () => {
  const naviagator = useNavigate();

  useEffect(() => {
    sessionStorage.getItem('loginId') === null ? naviagator('/login') : naviagator('/list');
  }, [])
  return (
    <>
      <Routes>
      <Route path='/login' exact element={<Login/>} />
      <Route path='/register' exact element={<Register/>} />
      <Route path='/enrollment' exact element={<Enrollment />}/>
      <Route path='/list' exact element={<CarList />} />
      <Route path='/streaming/:carId' exact element={<Streaming />}/>
    </Routes>
    </>
  );
}

export default App;
