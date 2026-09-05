package lk.sunrise.clinic;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ClinicApplicationContextTest {
    @Test
    void contextLoadsWithThreeTierComponentsWired() {
        // Successful context startup verifies Spring configuration, repositories, services and controllers can be wired.
    }
}
