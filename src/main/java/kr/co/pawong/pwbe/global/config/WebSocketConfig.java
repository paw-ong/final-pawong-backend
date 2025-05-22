package kr.co.pawong.pwbe.global.config;

import java.util.List;
import kr.co.pawong.pwbe.global.handshake.JwtHandshakeHandler;
import kr.co.pawong.pwbe.global.interceptor.JwtHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authorization.AuthorizationEventPublisher;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.SpringAuthorizationEventPublisher;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.AuthorizationChannelInterceptor;
import org.springframework.security.messaging.context.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.messaging.context.SecurityContextChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSocketSecurity
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;
    private final JwtHandshakeHandler jwtHandshakeHandler;
    private final ApplicationContext applicationContext;
    private final AuthorizationManager<Message<?>> authorizationManager;


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .setAllowedOriginPatterns("http://localhost", "https://pawong.co.kr", "*")
                .addInterceptors(jwtHandshakeInterceptor)
                .setHandshakeHandler(jwtHandshakeHandler)
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue");  // 클라이언트가 구독(subscribe)할 수 있는 목적지
        registry.setApplicationDestinationPrefixes("/app");       //  클라이언트 -> @MessageMapping 으로 라우팅
        registry.setUserDestinationPrefix("/user");               //  개별 사용자에게만 전송할 목적지(user destination)
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration reg) {
        AuthorizationEventPublisher publisher = new SpringAuthorizationEventPublisher(
                applicationContext);
        AuthorizationChannelInterceptor authorizationChannelInterceptor = new AuthorizationChannelInterceptor(
                authorizationManager);
        authorizationChannelInterceptor.setAuthorizationEventPublisher(publisher);

        reg
                .interceptors(
                        new SecurityContextChannelInterceptor(),
                        authorizationChannelInterceptor)
                .taskExecutor()
                .corePoolSize(4)
                .maxPoolSize(8)
                .queueCapacity(1000);
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration reg) {
        reg.taskExecutor()
                .corePoolSize(2)
                .maxPoolSize(4)
                .queueCapacity(500);
    }


}
