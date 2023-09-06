//package com.mobility.remotedrivingmobility_be.controller.member;
//
//import com.mobility.remotedrivingmobility_be.common.ResponseSuccessNoResult;
//import com.mobility.remotedrivingmobility_be.service.member.SignupService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import static com.mobility.remotedrivingmobility_be.common.ResConditionCode.*;
//import static com.mobility.remotedrivingmobility_be.dto.member.MemberReqDto.*;
//
//@RestController
//@RequestMapping("/api/members/signup")
//@RequiredArgsConstructor
//public class SignupApiController {
//    private final SignupService signupService;
//
//    @GetMapping("/check-id")
//    public ResponseSuccessNoResult isDuplicatedLoginId(@RequestParam String loginId) {
//        signupService.checkDuplicationLoginId(loginId);
//        return new ResponseSuccessNoResult(LOGIN_USABLE);
//    }
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseSuccessNoResult signup(@RequestBody @Validated SignupReq signupReq) {
//        signupService.signup(signupReq);
//        return new ResponseSuccessNoResult(SIGNUP_SUCCESS);
//    }
//}
