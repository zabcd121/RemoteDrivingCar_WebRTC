//package com.mobility.remotedrivingmobility_be.config.jwt;
//
//import com.mobility.remotedrivingmobility_be.config.jwt.exception.TokenNotBearerException;
//import com.mobility.remotedrivingmobility_be.config.jwt.exception.TokenNotPassedException;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.MalformedJwtException;
//import io.jsonwebtoken.UnsupportedJwtException;
//import io.jsonwebtoken.security.SignatureException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.util.ObjectUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//import static com.mobility.remotedrivingmobility_be.config.jwt.JwtTokenProvider.*;
//
//@Slf4j
//@RequiredArgsConstructor
//public class JwtTokenFilter extends OncePerRequestFilter {
//
//    private final JwtTokenProvider jwtTokenProvider;
//
//    private final RedisTemplate redisTemplate;
//
//    /**
//     * JWT를 검증하는 필터
//     * HttpServletRequest 의 Authorization 헤더에서 JWT token을 찾고 그것이 맞는지 확인
//     * UsernamePasswordAuthenticationFilter 앞에서 작동
//     * (JwtTokenFilterConfigurer 참고)
//     */
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        log.info("####doFilterInternal진입");
//        try{
//            String resolvedToken = jwtTokenProvider.resolveToken(request.getHeader(AUTHORIZATION_HEADER));
//            if (resolvedToken != null && jwtTokenProvider.validateToken(resolvedToken)) {
//                String isLogout = (String) redisTemplate.opsForValue().get(resolvedToken);
//                if (ObjectUtils.isEmpty(isLogout)) {
//                    Authentication authentication = jwtTokenProvider.getAuthentication(resolvedToken);
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                    log.info("set Authentication to security context for '{}', uri: {}", authentication.getName(), request.getRequestURI());
//                }
//            }
//
//        } catch(TokenNotPassedException e) {
//            request.setAttribute("exception", e);
//            log.info("catch token not passed exception");
//        } catch (TokenNotBearerException e) {
//            request.setAttribute("exception", e);
//            log.info("TokenNotBearerException");
//        } catch (SignatureException e) {
//            request.setAttribute("exception", e);
//            log.info("SignatureException");
//        } catch (MalformedJwtException e) {
//            request.setAttribute("exception", e);
//            log.info("MalformedJwtException");
//        } catch (ExpiredJwtException e) {
//            request.setAttribute("exception", e);
//            log.info("ExpiredJwtException");
//        } catch (UnsupportedJwtException e) {
//            request.setAttribute("exception", e);
//            log.info("UnsupportedJwtException");
//        } catch (JwtException | IllegalArgumentException e) {
//            request.setAttribute("exception", e);
//            log.info("JwtException IllegalArgumentException");
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
