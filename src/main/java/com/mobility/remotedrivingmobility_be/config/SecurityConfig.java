//package com.mobility.remotedrivingmobility_be.config;
//
//import com.mobility.remotedrivingmobility_be.config.jwt.JwtAccessDeniedHandler;
//import com.mobility.remotedrivingmobility_be.config.jwt.JwtAuthenticationEntryPoint;
//import com.mobility.remotedrivingmobility_be.config.jwt.JwtTokenFilter;
//import com.mobility.remotedrivingmobility_be.config.jwt.JwtTokenProvider;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
//@RequiredArgsConstructor
//@Configuration
//public class SecurityConfig {
//
//    private final JwtTokenProvider jwtTokenProvider;
//    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
//    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//    private final RedisTemplate redisTemplate;
//
//    @Bean // 인증 실패 처리 관련 객체
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//
//    @Bean // 비밀번호 암호화 객체
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    /**
//     * 현장예약 api는 인증과 동시에 예약이라 ignore해야 함.
//     */
//    @Bean
//    public WebSecurityCustomizer configure() {
//        return (web -> web.ignoring().mvcMatchers(
//                "/ws/**",
//                        "/api/members/token/reissue",
//                        "/api/members/login",
//                        "/api/admins/login",
//                        "/api/members/signup/**",
//                        "/api/images/**",
//                        "/actuator/**"
//                ));
//    }
//
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true); //내 서버가 응답을 할 때 json을 자바스크립트에서 처리할 수 있게 할지 설정하는 것,
//        config.addAllowedOriginPattern("*"); // e.g. http://domain1.com // *이면 모든 ip에 응답을 허용하겠다.
//        config.addAllowedHeader("*"); //모든 header에 응답을 허용하겠다.
//        config.addAllowedMethod("*"); //모든 post, get, put, delete, patch 요청을 허용하겠다.
//        config.addExposedHeader("Authorization");
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http
//                .addFilter(corsFilter())//@CrossOrigin(인증x), 시큐리티 필터에 등록 인증(o)
//                .csrf().disable()
//                .httpBasic().disable()
//                .formLogin().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
////                .exceptionHandling()
////                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 인증 실패시 오류 처리
////                .accessDeniedHandler(jwtAccessDeniedHandler) // 권한 부족시 오류 처리
////                .and()
//////                .apply(jwtTokenFilterConfig)
//////                .and()
////                .authorizeRequests()
////                .antMatchers("/api/admins/**")
////                .access("hasRole('ROLE_ADMIN')")
////                .antMatchers("/api/**")
////                .access("hasRole('ROLE_MEMBER') or hasRole('ROLE_ADMIN')")
////                .and()
////                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider, redisTemplate), UsernamePasswordAuthenticationFilter.class)
//                .build();
//
//    }
//}
