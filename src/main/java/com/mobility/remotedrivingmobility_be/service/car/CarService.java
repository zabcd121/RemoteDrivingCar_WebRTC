package com.mobility.remotedrivingmobility_be.service.car;

import com.mobility.remotedrivingmobility_be.config.jwt.JwtTokenProvider;
import com.mobility.remotedrivingmobility_be.domain.car.Car;
import com.mobility.remotedrivingmobility_be.dto.car.CarReqDto;
import com.mobility.remotedrivingmobility_be.dto.car.CarResDto;
import com.mobility.remotedrivingmobility_be.repository.car.CarRepository;
import com.mobility.remotedrivingmobility_be.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static com.mobility.remotedrivingmobility_be.config.jwt.JwtTokenProvider.*;
import static com.mobility.remotedrivingmobility_be.dto.car.CarReqDto.*;
import static com.mobility.remotedrivingmobility_be.dto.car.CarResDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarService {

    private final MemberRepository memberRepository;
    private final CarRepository carRepository;

    @Transactional
    public void registrationCar(Long memberId, CarRegistrationReq carRegistrationReq) {
        carRepository.save(
                Car.createCar(
                        memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다.")),
                        carRegistrationReq.getCarNumber())
        );
    }

    public List<CarSearchRes> searchMyCar(Long memberId) {
        List<CarSearchRes> carSearchResList = new ArrayList<>();
        carRepository.findAllBy(memberId)
                .forEach(car -> carSearchResList.add(
                        CarSearchRes.of(car)));

        return carSearchResList;
    }
}
