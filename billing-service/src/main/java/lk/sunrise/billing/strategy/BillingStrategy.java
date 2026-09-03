package lk.sunrise.billing.strategy;

import java.math.BigDecimal;

public interface BillingStrategy {
    BigDecimal calculateTotal(BigDecimal treatmentCost, BigDecimal consultationFee);
}
