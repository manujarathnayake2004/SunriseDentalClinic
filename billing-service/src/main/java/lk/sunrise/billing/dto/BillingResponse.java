package lk.sunrise.billing.dto;

import java.math.BigDecimal;

public record BillingResponse(
        BigDecimal treatmentCost,
        BigDecimal consultationFee,
        BigDecimal total
) {}
