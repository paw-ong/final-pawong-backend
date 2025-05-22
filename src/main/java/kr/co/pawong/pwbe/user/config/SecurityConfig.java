package kr.co.pawong.pwbe.user.config;

import kr.co.pawong.pwbe.user.adapter.out.security.error.CustomAuthenticationEntryPoint;
import kr.co.pawong.pwbe.user.adapter.out.security.CustomOAuth2UserService;
import kr.co.pawong.pwbe.user.adapter.out.security.JwtTokenProvider;
import kr.co.pawong.pwbe.user.adapter.out.security.OAuth2AuthenticationSuccessHandler;
import kr.co.pawong.pwbe.user.adapter.out.security.filter.JwtFilter;
import kr.co.pawong.pwbe.user.application.port.in.QueryUserDataUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final QueryUserDataUseCase queryUserDataUseCase;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public SecurityConfig(
            JwtFilter jwtFilter,
            JwtTokenProvider jwtTokenProvider,
            UserDetailsService userDetailsService,
            CustomOAuth2UserService customOAuth2UserService,
            QueryUserDataUseCase queryUserDataUseCase,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint
    ) {
        this.jwtFilter = jwtFilter;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.customOAuth2UserService = customOAuth2UserService;
        this.queryUserDataUseCase = queryUserDataUseCase;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // JS에서 읽을 수 있도록 HttpOnly=false
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
                                "/api/lost-animals/**",
                                "/api/auth/csrf-token"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.GET, "/api/lost-animals/*" // ⬅ 단건 조회만 허용
                        ).permitAll()
                        .anyRequest().authenticated())

                // oauth2 요청만 처리
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler())
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint));

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(queryUserDataUseCase, jwtTokenProvider);
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

