package kr.co.pawong.pwbe.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

@Configuration
@EnableWebSocketSecurity
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    /**
     * STOMP 메시지 보안 규칙 설정:
     *  - CONNECT/DISCONNECT 는 모두 허용
     *  - /app/** 로 보내는 SEND 프레임은 인증된 사용자만
     *  - /user/queue/chat/** 구독도 인증된 사용자만
     *  - 그 외 메시지 전부 차단
     */
    @Bean
    public AuthorizationManager<Message<?>> messageAuthorizationManager(
            MessageMatcherDelegatingAuthorizationManager.Builder messages
    ) {
        messages
                .nullDestMatcher().permitAll()
                .simpTypeMatchers(
                        SimpMessageType.CONNECT,
                        SimpMessageType.CONNECT_ACK,
                        SimpMessageType.SUBSCRIBE,
                        SimpMessageType.DISCONNECT
                ).permitAll()
                .simpDestMatchers("/app/**").authenticated()
                .simpSubscribeDestMatchers("/user/queue/chat/**").authenticated();
//                .anyMessage().denyAll();
        return messages.build();
    }

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages.simpDestMatchers("/ws/**").authenticated();
    }

}