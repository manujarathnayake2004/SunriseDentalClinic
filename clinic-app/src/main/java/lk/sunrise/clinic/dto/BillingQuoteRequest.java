package lk.sunrise.clinic.dto;

import java.math.BigDecimal;

public record BillingQuoteRequest(BigDecimal treatmentCost, BigDecimal consultationFee) {
}
