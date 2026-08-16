package com.katerin.farmacia;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ServletInitializerTest {
    private static class TestableServletInitializer extends ServletInitializer {
        SpringApplicationBuilder callConfigure(SpringApplicationBuilder builder) {
            return super.configure(builder);
        }
    }

    @Test
    void testConfigure() {
        TestableServletInitializer servletInitializer = new TestableServletInitializer();
        SpringApplicationBuilder builder = mock(SpringApplicationBuilder.class);

        when(builder.sources(FarmaciaApplication.class)).thenReturn(builder);

        SpringApplicationBuilder result = servletInitializer.callConfigure(builder);

        assertNotNull(result);
        verify(builder).sources(FarmaciaApplication.class);
    }
}