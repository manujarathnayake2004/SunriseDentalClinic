package lk.sunrise.billing.service;

import lk.sunrise.billing.dto.BillingRequest;
import lk.sunrise.billing.dto.BillingResponse;
import lk.sunrise.billing.strategy.BillingStrategy;
import org.springframework.stereotype.Service;

@Service
public class BillingCalculationService {
    private final BillingStrategy billingStrategy;

    public BillingCalculationService(BillingStrategy billingStrategy) {
        this.billingStrategy = billingStrategy;
    }

    public BillingResponse calculate(BillingRequest request) {
        return new BillingResponse(
                request.treatmentCost(),
                request.consultationFee(),
                billingStrategy.calculateTotal(request.treatmentCost(), request.consultationFee())
        );
    }
}
