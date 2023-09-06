<!-- PROJECT LOGO -->
<br />
<div align="center">
<img width="394" alt="스크린샷 2022-11-21 오후 3 48 47" src="https://user-images.githubusercontent.com/68465716/265900178-a9fb35ca-823c-417c-8cd8-640237597f2a.png">
<h3 align="center">WebRTC를 이용한 차량 원격 주행 기술</h3>
  <p align="center">
    WebRTC 실시간 양방향 통신 기술을 이용하여 웹을 통해 차량을 원격 모니터링 및 조작하는 플랫폼입니다.
  </p>
</div>
<div align=center><h1>📚 BACKEND STACKS</h1>
<img src="https://img.shields.io/badge/java-0174DF?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/javascript-2E64FE?style=for-the-badge&logo=javascript&logoColor=white">
<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
<br>
<img src="https://img.shields.io/badge/WebRTC-F78181?style=for-the-badge&logo=WebRTC&logoColor=white">
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
<img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
<br>
<img src="https://img.shields.io/badge/mariaDB-003545?style=for-the-badge&logo=mariaDB&logoColor=white">
<img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
<br>
<img src="https://img.shields.io/badge/ubuntu-E95420?style=for-the-badge&logo=ubuntu&logoColor=white">
<img src="https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=Nginx&logoColor=white">
<img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
</div>
<div>
<hr>
    <div style="align-content: center; padding: 0px 200px;">
        <ol>
            <li><a href="#purpose">✅ 시스템 목적</a></li>
            <li><a href="#introduce">💁🏻‍ 기여한 부분</a></li>
            <li><a href="#structure">🏗️ 서버 아키텍처 및 사양</a></li>
            <li><a href="#erd">🛢️ ERD</a></li>
            <li><a href="#video">‍🎥 주요 기능 시연 영상</a></li>
            <li><a href="#ui">🎨 UI 구현</a></li>
        </ol>
<hr>
<h2 id="purpose">✅ 시스템 요약</h2>
<p>- WebRTC 실시간 양방향 통신 기술을 이용하여 웹을 통해 차량을 원격 모니터링 및 조작하는 플랫폼입니다.</p>
<p>- 클라이언트는 실시간으로 차량에 부착된 카메라를 통해 주행 상황을 모니터링 할 수 있습니다.</p>
<p>- 컨트롤러로부터 얻은 제어 신호가 웹을 통해 차량으로 전달되고, 제어 신호를 받은 차량의 동작이 제어됩니다.</p>
        <hr>
<h1 id="introduce">💁🏻‍ 기여한 부분</h1>
<p style="font-size: 16px">1. Web Socket을 이용하여 시그널링 서버 개발</p>
      <p>WebRTC로 클라이언트와 차량을 연결하기 전에 차량과 클라이언트 각자 Google Stun Server를<br> 통해 조회한 (ip 주소, 포트)의 조합으로 구성된 연결 가능한 네트워크 주소 ICE를 서로 주고받을 수 있도록 WebSocket을 이용하여 시그널링 서버를 구현했습니다.</p>
<p style="font-size: 16px">2. WebRTC 기반 주행 영상 데이터 및 컨트롤러 입력 값 전달 및 수신 기능 개발</p>
      <p>차량에 연결된 전방과 양 사이드에 달린 총 3대의 카메라의 영상을 실시간으로 Client에게 전송하고,<br> 역으로 클라이언트에게서 입력된 컨트롤러 입력 값을 차량으로 전송하도록 실시간 양방향 통신 기술인 WebRTC를 이용하여 개발했습니다. 4G 환경에서 테스트 했을 때 영상 Delay Time 0.3초를 달성했습니다. </p>
<hr>
<h1 id="structure">🏗️ 서버 아키텍처 및 사양</h1>
<h3>(1) 운영서버 : On-premise <br><br>(OS: ubuntu20.04, EC2 instance: t2.small, vCPU: 1, RAM: 2GB)</h3>
<div align="center">
<img src="https://user-images.githubusercontent.com/68465716/265900167-775e8832-f8b3-45d4-a7de-604a4bf9c2bf.png" align="center"></img>
</div>
<br>
</div>
<div style="align-content: center; padding: 0px 200px;">
<hr>
<h1 id="erd">🛢️ ERD</h1>
<div align="center">
<img src="https://user-images.githubusercontent.com/68465716/265900191-000a1549-2354-4190-9aca-680cdd5d8561.png">
</div>
<br>
<hr>
    <h1 id="video">🎥 주요 기능 시연 영상</h1>
    <h2> 1️⃣ 주행 모니터링 영상 </h2>
    <video src="https://user-images.githubusercontent.com/68465716/265900091-3c64cd44-4f82-4dd9-a41a-1a596ef297b6.mp4"></video>
    <br><br>
    <h2> 2️⃣ 차량 주행 영상 </h2>
    <video src="https://user-images.githubusercontent.com/68465716/265900064-e3c4e025-0ee6-4dff-911f-4ac762bc256e.mp4"></video>
    <br><br>
    <h2> 3️⃣ 등화장치 조작 영상 </h2>
    <video src="https://user-images.githubusercontent.com/68465716/265900041-d7e542b2-eb2d-4a23-b8bd-2dcefca2e095.mp4"></video>
    <br>
<hr>
<h1 id="ui">🎨 UI 구현</h1>
<div>
<h2>1. 로그인</h2>
<img src="https://user-images.githubusercontent.com/68465716/265900153-92ebbcfa-e412-4fb0-8f9c-cfe631ca9600.png" height="500">
<h2>2. 모니터링 화면</h2>
<img src="https://user-images.githubusercontent.com/68465716/265900162-ed4a05ef-cf24-4b16-8eb6-7533c28d58c9.png" height="500">

</div>
</div>
