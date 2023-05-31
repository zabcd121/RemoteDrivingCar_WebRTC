//package com.mobility.remotedrivingmobility_be.config.jwt;
//
//import com.mobility.remotedrivingmobility_be.domain.member.Member;
//import com.mobility.remotedrivingmobility_be.repository.member.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class MemberDetailsService implements UserDetailsService {
//
//    private final MemberRepository memberRepository;
//
//    /**
//     * Spring-Security의 유저 인증 처리 과정중 유저객체를 만드는 과정
//     * !! 보통 구글링시 UserDetails 클래스를 따로 만들어서 사용하지만 UserDetails 인터페이스를 구현한
//     * User 라는 클래스를 시큐리티가 제공해줘서 굳이 만들어주지 않음
//     * @param username == loginId
//     * @return UserDetails (security에서 사용하는 유저 정보를 가진 객체)
//     * @throws UsernameNotFoundException userId로 유저를 찾지 못했을 경우 발생하는 에러
//     */
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        System.out.println("MemberDetailsService loadUserByUsername: 진입");
//        Member member = memberRepository.findById(Long.parseLong(username))
//                .orElseThrow(() -> new UsernameNotFoundException("memberId : " + username + " was not found"));
//
//        log.info("loadUserByUsername 통과 ");
//        return createUserDetails(member);
//    }
//
//    private UserDetails createUserDetails(Member member) {
//        List<SimpleGrantedAuthority> grantedAuthorities = member.getAccount().getAuthorities().stream()
//                .map(authority -> new SimpleGrantedAuthority(authority))
//                .collect(Collectors.toList());
//
//        return new User(member.getId().toString(),
//                member.getAccount().getPassword(),
//                grantedAuthorities);
//    }
//
//}
