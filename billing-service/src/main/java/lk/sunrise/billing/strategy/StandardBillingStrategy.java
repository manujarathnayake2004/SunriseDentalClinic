package lk.sunrise.billing.strategy;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class StandardBillingStrategy implements BillingStrategy {
    @Override
    public BigDecimal calculateTotal(BigDecimal treatmentCost, BigDecimal consultationFee) {
        return treatmentCost.add(consultationFee);
    }
}
