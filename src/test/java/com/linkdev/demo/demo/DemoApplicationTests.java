package com.linkdev.demo.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
class DemoApplicationTests {

    @Test
    void healthReturnsOk() {
        HealthController c = new HealthController();
        assertThat(c.health()).isEqualTo("OK");
    }

}
