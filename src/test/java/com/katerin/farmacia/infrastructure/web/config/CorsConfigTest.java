package com.katerin.farmacia.infrastructure.web.config;


import static org.junit.jupiter.api.Assertions.assertNotNull;


import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistry;


public class CorsConfigTest {
    @Test
    public void shouldConfigureCorsMapping(){
        CorsConfig corsConfig = new CorsConfig();
        CorsRegistry registry = new CorsRegistry();
        corsConfig.addCorsMappings(registry);


        assertNotNull(registry);
    }
}


