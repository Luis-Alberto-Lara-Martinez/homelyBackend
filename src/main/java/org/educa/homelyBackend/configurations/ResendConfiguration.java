package org.educa.homelyBackend.configurations;

import com.resend.Resend;
import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.properties.ResendProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ResendConfiguration {

    private final ResendProperties resendProperties;

    @Bean
    public Resend resend() {
        return new Resend(resendProperties.apiKey());
    }
}
