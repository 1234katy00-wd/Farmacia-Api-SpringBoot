package com.katerin.farmacia;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class FarmaciaApplicationTests {
    
    @Test
    void contextLoads(){

    }

    @Test
    void mainMethodTest(){
        FarmaciaApplication.main(new String[] {"--server.port=0"});
    }
}
