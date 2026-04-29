package org.educa.homelyBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class HomelyBackendApplication {

    static void main() {
        SpringApplication.run(HomelyBackendApplication.class);
    }
}