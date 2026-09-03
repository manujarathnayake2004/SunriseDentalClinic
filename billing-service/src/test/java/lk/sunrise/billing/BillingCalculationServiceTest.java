package lk.sunrise.billing;

import lk.sunrise.billing.dto.BillingRequest;
import lk.sunrise.billing.service.BillingCalculationService;
import lk.sunrise.billing.strategy.StandardBillingStrategy;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BillingCalculationServiceTest {
    @Test
    void shouldAddTreatmentCostAndConsultationFee() {
        BillingCalculationService service = new BillingCalculationService(new StandardBillingStrategy());

        var response = service.calculate(new BillingRequest(
                new BigDecimal("5000.00"),
                new BigDecimal("2000.00")
        ));

        assertEquals(new BigDecimal("7000.00"), response.total());
        assertEquals(new BigDecimal("5000.00"), response.treatmentCost());
        assertEquals(new BigDecimal("2000.00"), response.consultationFee());
    }
}
