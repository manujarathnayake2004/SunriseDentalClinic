package lk.sunrise.clinic.service;

import lk.sunrise.clinic.dto.BillingQuoteRequest;
import lk.sunrise.clinic.dto.BillingQuoteResponse;
import lk.sunrise.clinic.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;

@Component
public class BillingClient {
    private final RestClient restClient;
    private final String billingServiceUrl;

    public BillingClient(RestClient restClient,
                         @Value("${billing.service.url:http://localhost:8081}") String billingServiceUrl) {
        this.restClient = restClient;
        this.billingServiceUrl = billingServiceUrl;
    }

    public boolean isAvailable() {
        try {
            restClient.get()
                    .uri(billingServiceUrl + "/api/billing/health")
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (RestClientException ex) {
            return false;
        }
    }

    public BillingQuoteResponse calculate(BigDecimal treatmentCost, BigDecimal consultationFee) {
        try {
            BillingQuoteResponse response = restClient.post()
                    .uri(billingServiceUrl + "/api/billing/calculate")
                    .body(new BillingQuoteRequest(treatmentCost, consultationFee))
                    .retrieve()
                    .body(BillingQuoteResponse.class);
            if (response == null) throw new BusinessException("Billing service returned an empty response.");
            return response;
        } catch (RestClientException ex) {
            throw new BusinessException("Billing web service is unavailable. Start the billing-service on port 8081.");
        }
    }
}
