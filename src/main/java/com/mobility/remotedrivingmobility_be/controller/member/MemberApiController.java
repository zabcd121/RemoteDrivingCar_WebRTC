//package com.mobility.remotedrivingmobility_be.controller.member;
//
//import com.mobility.remotedrivingmobility_be.common.ResConditionCode;
//import com.mobility.remotedrivingmobility_be.common.ResponseSuccess;
//import com.mobility.remotedrivingmobility_be.config.jwt.JwtTokenProvider;
//import com.mobility.remotedrivingmobility_be.dto.member.MemberResDto;
//import com.mobility.remotedrivingmobility_be.service.member.MemberSearchService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//
//import static com.mobility.remotedrivingmobility_be.common.ResConditionCode.*;
//import static com.mobility.remotedrivingmobility_be.config.jwt.JwtTokenProvider.*;
//import static com.mobility.remotedrivingmobility_be.dto.member.MemberResDto.*;
//
//@RestController
//@RequestMapping("/api/members")
//@RequiredArgsConstructor
//public class MemberApiController {
//    private final MemberSearchService memberSearchService;
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @GetMapping
//    public ResponseSuccess<MemberSimpleInfoRes> getMemberProfile(HttpServletRequest request) {
//        String resolvedToken = jwtTokenProvider.resolveToken(request.getHeader(AUTHORIZATION_HEADER));
//
//        MemberSimpleInfoRes memberProfile = memberSearchService.searchMyPageInfo(
//                Long.parseLong(jwtTokenProvider.getSubject(resolvedToken)));
//
//        return new ResponseSuccess(MEMBER_MY_PAGE_INFO_SEARCH_SUCCESS, memberProfile);
//    }
//
//}