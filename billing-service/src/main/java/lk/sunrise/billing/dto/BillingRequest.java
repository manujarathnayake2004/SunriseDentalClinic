package lk.sunrise.billing.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record BillingRequest(
        @NotNull @DecimalMin("0.00") BigDecimal treatmentCost,
        @NotNull @DecimalMin("0.00") BigDecimal consultationFee
) {}
