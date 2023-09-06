package com.mobility.remotedrivingmobility_be.controller.car;

import com.mobility.remotedrivingmobility_be.common.ResConditionCode;
import com.mobility.remotedrivingmobility_be.common.ResponseSuccess;
import com.mobility.remotedrivingmobility_be.common.ResponseSuccessNoResult;
import com.mobility.remotedrivingmobility_be.config.jwt.JwtTokenProvider;
import com.mobility.remotedrivingmobility_be.dto.car.CarReqDto;
import com.mobility.remotedrivingmobility_be.dto.car.CarResDto;
import com.mobility.remotedrivingmobility_be.service.car.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static com.mobility.remotedrivingmobility_be.common.ResConditionCode.*;
import static com.mobility.remotedrivingmobility_be.config.jwt.JwtTokenProvider.AUTHORIZATION_HEADER;
import static com.mobility.remotedrivingmobility_be.dto.car.CarReqDto.*;
import static com.mobility.remotedrivingmobility_be.dto.car.CarResDto.*;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarApiController {

    private final CarService carService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseSuccessNoResult registrationCar(@RequestBody CarRegistrationReq carRegistrationReq, HttpServletRequest request) {
        String resolvedToken = jwtTokenProvider.resolveToken(request.getHeader(AUTHORIZATION_HEADER));
        carService.registrationCar(
                Long.parseLong(jwtTokenProvider.getSubject(resolvedToken)),
                carRegistrationReq
        );

        return new ResponseSuccessNoResult(CAR_REGISTRATION_SUCCESS);
    }

    @GetMapping
    public ResponseSuccess<List<CarSearchRes>> searchMyCar(HttpServletRequest httpServletRequest) {
        String resolvedToken = jwtTokenProvider.resolveToken(httpServletRequest.getHeader(AUTHORIZATION_HEADER));
        List<CarSearchRes> myCarList = carService.searchMyCar(Long.parseLong(jwtTokenProvider.getSubject(resolvedToken)));

        return new ResponseSuccess(CAR_SEARCH_SUCCESS, myCarList);
    }
}
