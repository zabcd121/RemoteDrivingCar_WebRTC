//package com.mobility.remotedrivingmobility_be.service.member;
//
//import com.mobility.remotedrivingmobility_be.domain.member.Member;
//import com.mobility.remotedrivingmobility_be.exception.member.*;
//import com.mobility.remotedrivingmobility_be.repository.member.SignupRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import static com.mobility.remotedrivingmobility_be.domain.member.RoleType.*;
//import static com.mobility.remotedrivingmobility_be.dto.member.MemberReqDto.*;
//
//@Service
//@Slf4j
//@Transactional(readOnly = true)
//@RequiredArgsConstructor
//public class SignupService {
//    private final SignupRepository signupRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Transactional
//    public void signup(SignupReq signupReq) {
//        checkDuplicationLoginId(signupReq.getLoginId());
//
//        Member signupMember = Member.createMember(
//                signupReq.getLoginId(),
//                passwordEncoder.encode(signupReq.getPassword()),
//                signupReq.getName(),
//                ROLE_MEMBER);
//
//        signupRepository.save(signupMember);
//    }
//
//    public void checkDuplicationLoginId(String loginId) {
//        if(signupRepository.existsByAccountLoginId(loginId)) {
//            throw new LoginIdDuplicatedException();
//        }
//    }
//}
