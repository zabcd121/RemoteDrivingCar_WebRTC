//package com.mobility.remotedrivingmobility_be.controller.member;
//
//
//import com.mobility.remotedrivingmobility_be.common.ResponseSuccess;
//import com.mobility.remotedrivingmobility_be.common.ResponseSuccessNoResult;
//import com.mobility.remotedrivingmobility_be.config.jwt.JwtTokenProvider;
//import com.mobility.remotedrivingmobility_be.service.member.AuthService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//
//import static com.mobility.remotedrivingmobility_be.common.ResConditionCode.*;
//import static com.mobility.remotedrivingmobility_be.config.jwt.JwtTokenProvider.*;
//import static com.mobility.remotedrivingmobility_be.domain.member.RoleType.*;
//import static com.mobility.remotedrivingmobility_be.dto.member.MemberReqDto.*;
//import static com.mobility.remotedrivingmobility_be.dto.member.MemberResDto.*;
//import static com.mobility.remotedrivingmobility_be.dto.member.TokenDto.*;
//
//@RestController
//@RequestMapping("/api/members")
//@RequiredArgsConstructor
//@Slf4j
//public class MemberAuthApiController {
//
//    private final AuthService authService;
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @PostMapping("/login")
//    public ResponseSuccess<LoginRes> login(@RequestBody @Validated LoginReq loginReq) {
//        LoginRes loginResponse = authService.login(loginReq, ROLE_MEMBER);
//        return new ResponseSuccess(LOGIN_SUCCESS, loginResponse);
//    }
//
//    @DeleteMapping("/logout")
//    public ResponseSuccessNoResult logout(HttpServletRequest request) {
//        String resolvedToken = jwtTokenProvider.resolveToken(request.getHeader(AUTHORIZATION_HEADER));
//        authService.logout(resolvedToken);
//        return new ResponseSuccessNoResult(LOGOUT_SUCCESS);
//    }
//
//    /**
//     * Access token이 만료되었을 경우 재발급 요청 api
//     * @header - Authorization에 : Access Token 대신 Refresh token을 넣어서 준다.
//     * @return TokenResponseDto : Access token과 Refresh token 모두 재발급해준다.
//     */
//    @PostMapping("/token/reissue")
//    public ResponseSuccess<TokensDto> reissueToken(HttpServletRequest request){
//        TokensDto tokensDto = authService.reissueToken(
//                jwtTokenProvider.resolveToken(request.getHeader(AUTHORIZATION_HEADER)));
//        return new ResponseSuccess(TOKEN_REISSUED, tokensDto);
//    }
//
//    @GetMapping("/reissue")
//    public ResponseSuccess<LoginMemberInfoRes> refreshMemberInfo(HttpServletRequest request) {
//        String resolvedToken = jwtTokenProvider.resolveToken(request.getHeader(AUTHORIZATION_HEADER));
//        LoginMemberInfoRes memberInfo = authService.searchMemberInfo(Long.parseLong(jwtTokenProvider.getSubject(resolvedToken)));
//        return new ResponseSuccess<>(MEMBER_INFO_REISSUED, memberInfo);
//    }
//}
