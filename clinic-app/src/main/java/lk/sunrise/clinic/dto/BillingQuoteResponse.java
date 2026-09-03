package lk.sunrise.clinic.dto;

import java.math.BigDecimal;

public record BillingQuoteResponse(BigDecimal treatmentCost, BigDecimal consultationFee, BigDecimal total) {
}
