package kr.co.pawong.pwbe.global.config;

import kr.co.pawong.pwbe.global.security.error.CustomAuthenticationEntryPoint;
import kr.co.pawong.pwbe.global.security.filter.JwtFilter;
import kr.co.pawong.pwbe.global.security.handler.CookieClearingLogoutSuccessHandler;
import kr.co.pawong.pwbe.global.security.handler.OAuth2AuthenticationSuccessHandler;
import kr.co.pawong.pwbe.global.security.handler.RedisLogoutHandler;
import kr.co.pawong.pwbe.global.security.service.CustomOAuth2UserService;
import kr.co.pawong.pwbe.global.security.util.JwtTokenProvider;
import kr.co.pawong.pwbe.user.application.port.in.QueryUserDataUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final QueryUserDataUseCase queryUserDataUseCase;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final RedisLogoutHandler redisLogoutHandler;
    private final CookieClearingLogoutSuccessHandler cookieClearingLogoutSuccessHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                                .ignoringRequestMatchers("/api/**")
                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        // JS에서 읽을 수 있도록 HttpOnly=false
                )

                // ExceptionTranslationFilter -> jwtFilter 실행
                .addFilterAfter(jwtFilter, ExceptionTranslationFilter.class)

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/oauth2/authorization/**",     // OAuth2 로그인 요청
                                "/login/oauth2/**",             // OAuth2 code Redirect URI
                                "/oauth/authorize"              // OAuth2 Authorization Endpoint
                        ).permitAll()                         // 위 경로는 인증 없이 접근 가능
                        .requestMatchers(
                                "/ws/**",
                                "/info",
                                "/api/auth/csrf-token"
                        ).permitAll()
                        .requestMatchers(
                                "/api/adoptions/**",
                                "/api/adoption/**",
                                "/api/shelters/**",
                                "/api/lost-animals/**"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.GET, "/api/lost-animals/*" // ⬅ 단건 조회만 허용
                        ).permitAll()
                        .anyRequest().authenticated())

                // oauth2 요청만 처리
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint))

                .logout(logout -> logout
                    .logoutUrl("/api/auth/logout")
                    .addLogoutHandler(redisLogoutHandler)
                    .logoutSuccessHandler(cookieClearingLogoutSuccessHandler));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

