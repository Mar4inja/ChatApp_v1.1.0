package de.ait.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Aktivizē vienkāršu atmiņas brokeri
        config.setApplicationDestinationPrefixes("/app"); // Norāda prefiksu ziņojumiem
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Atļauj pieprasījumus no localhost:5500
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:5500", "http://localhost:3000") // Šeit jānorāda pareizais origin
                .withSockJS(); // Atļaut SockJS
    }
}
