//package com.mobility.remotedrivingmobility_be.service.member;
//
//
//import com.mobility.remotedrivingmobility_be.config.jwt.JwtTokenProvider;
//import com.mobility.remotedrivingmobility_be.config.jwt.MemberDetailsService;
//import com.mobility.remotedrivingmobility_be.domain.member.Member;
//import com.mobility.remotedrivingmobility_be.domain.member.RoleType;
//import com.mobility.remotedrivingmobility_be.exception.member.*;
//import com.mobility.remotedrivingmobility_be.repository.member.MemberRepository;
//import com.mobility.remotedrivingmobility_be.repository.member.MemberSearchableRepository;
//import io.jsonwebtoken.ExpiredJwtException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.*;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.concurrent.TimeUnit;
//
//import static com.mobility.remotedrivingmobility_be.config.RedisConfig.*;
//import static com.mobility.remotedrivingmobility_be.config.jwt.JwtTokenProvider.BEARER;
//import static com.mobility.remotedrivingmobility_be.dto.member.MemberReqDto.*;
//import static com.mobility.remotedrivingmobility_be.dto.member.MemberResDto.*;
//import static com.mobility.remotedrivingmobility_be.dto.member.TokenDto.*;
//
//@Service
//@Slf4j
//@Transactional(readOnly = true)
//@RequiredArgsConstructor
//public class AuthService {
//
//    private final MemberRepository memberRepository;
//    private final MemberSearchableRepository memberSearchableRepository;
//    private final JwtTokenProvider jwtTokenProvider;
//    private final MemberDetailsService memberDetailsService;
//    private final RedisTemplate redisTemplate;
//    private final PasswordEncoder passwordEncoder;
//
//    @Transactional
//    public LoginRes login(LoginReq loginReq, RoleType allowedRoleType) {
//        Member member = memberSearchableRepository.findByAccountLoginId(loginReq.getLoginId()).orElseThrow(() -> new UsernameNotFoundException("memberId : " + loginReq.getLoginId() + " was not found"));
//        Authentication authentication = authenticateMember(member, loginReq.getPassword(), allowedRoleType);
//        String accessToken = jwtTokenProvider.createAccessToken(authentication);
//        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);
//        saveRefreshTokenInRedis(refreshToken, authentication);
//
//        return LoginRes.builder()
//                .memberInfo(getLoginMemberInfoResponse(member))
//                .tokenInfo(getTokensDto(accessToken, refreshToken))
//                .build();
//    }
//
//    @Transactional
//    public TokensDto reissueToken(String resolvedRefreshToken) {
//        log.info("refresh Token: {}", resolvedRefreshToken);
//
//        try {
//            jwtTokenProvider.validateToken(resolvedRefreshToken);
//        } catch (ExpiredJwtException ex) {
//            throw new RefreshTokenIsExpiredException();
//        }
//
//        Authentication authentication = jwtTokenProvider.getAuthentication(resolvedRefreshToken);
//
//        String findRedisRefreshToken = (String) redisTemplate.opsForValue().get(RT + authentication.getName());
//
//        if (!resolvedRefreshToken.equals(findRedisRefreshToken)) {
//            throw new NotExistsEqualRefreshTokenException();
//        }
//
//        String newAccessToken = jwtTokenProvider.createAccessToken(authentication);
//        String newRefreshToken = jwtTokenProvider.createRefreshToken(authentication);
//        redisTemplate.opsForValue().set(
//                RT + authentication.getName(),
//                newRefreshToken,
//                jwtTokenProvider.getExpiration(newRefreshToken),
//                TimeUnit.MILLISECONDS);
//
//
//        return TokensDto.builder()
//                .accessToken(BEARER + newAccessToken)
//                .refreshToken(BEARER + newRefreshToken)
//                .build();
//    }
//
//    /**
//     * logout시에 Redis에 해당 멤버의 refreshToken이 있다면 삭제하고 accessToken을 key로 남은 유효기간동안 Redis에 "logout" value와 함께 저장한다.
//     */
//    @Transactional
//    public void logout(String resolvedAccessToken) {
//        Authentication authentication = jwtTokenProvider.getAuthentication(resolvedAccessToken);
//
//        if(redisTemplate.opsForValue().get(RT + authentication.getName()) != null) {
//            redisTemplate.delete(RT + authentication.getName());
//        }
//
//        // 남은 유효시간동안만 블랙리스트에 저장하고 그 뒤에는 자동으로 삭제되도록 하면 후에 해커가 탈취한 토큰으로 로그인을 시도하더라도 만료돼서 인증 실패함
//        Long remainedExpiration = jwtTokenProvider.getExpiration(resolvedAccessToken);
//        redisTemplate.opsForValue()
//                .set(resolvedAccessToken, "logout", remainedExpiration, TimeUnit.MILLISECONDS);
//    }
//
//    public LoginMemberInfoRes searchMemberInfo(Long memberId) {
//        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotExistsMemberException());
//
//        return LoginMemberInfoRes.builder()
//                .memberId(member.getId())
//                .name(member.getName())
//                .roleType(member.getAccount().getRoleType())
//                .build();
//    }
//
//    private Authentication authenticateMember(Member member, String password, RoleType allowedRoleType){
//
//        UserDetails userDetails = memberDetailsService.loadUserByUsername(member.getId().toString());
//
//        matchPassword(password, userDetails.getPassword());
//
//        String role = "";
//        for (GrantedAuthority authority : userDetails.getAuthorities()) {
//            role = authority.getAuthority();
//            log.info("role = {}", role);
//        }
//
//        if(!role.equals(allowedRoleType.toString())) {
//            throw new NoAuthorityToLoginException();
//        }
//
//        return new UsernamePasswordAuthenticationToken(
//                userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
//    }
//
//    private void saveRefreshTokenInRedis(String refreshToken, Authentication authentication) {
//        redisTemplate.opsForValue()
//                .set(RT + authentication.getName(), refreshToken, jwtTokenProvider.getExpiration(refreshToken), TimeUnit.MILLISECONDS);
//    }
//
//    private LoginMemberInfoRes getLoginMemberInfoResponse(Member member) {
//        return LoginMemberInfoRes.builder()
//                .memberId(member.getId())
//                .name(member.getName())
//                .roleType(member.getAccount().getRoleType())
//                .build();
//    }
//
//    private TokensDto getTokensDto(String accessToken, String refreshToken) {
//        return TokensDto.builder()
//                .accessToken(BEARER + accessToken)
//                .refreshToken(BEARER + refreshToken)
//                .build();
//    }
//
//    private boolean matchPassword(String requestPassword, String originPassword) {
//        if(!passwordEncoder.matches(requestPassword, originPassword)) {
//            throw new InvalidPasswordException();
//        }
//        return true;
//    }
//}
