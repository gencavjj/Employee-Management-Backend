package com;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Smoke test to verify the Spring application context loads successfully.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
@ActiveProfiles("h2")
public class TestMain {

    @Test
    public void contextLoads() {
        // Verifies that the application context loads without errors
    }

}
