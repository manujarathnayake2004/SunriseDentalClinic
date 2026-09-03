package lk.sunrise.billing.controller;

import jakarta.validation.Valid;
import lk.sunrise.billing.dto.BillingRequest;
import lk.sunrise.billing.dto.BillingResponse;
import lk.sunrise.billing.service.BillingCalculationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing")
public class BillingApiController {
    private final BillingCalculationService service;

    public BillingApiController(BillingCalculationService service) {
        this.service = service;
    }

    @PostMapping("/calculate")
    public BillingResponse calculate(@Valid @RequestBody BillingRequest request) {
        return service.calculate(request);
    }

    @GetMapping("/health")
    public String health() {
        return "Billing service is running";
    }
}
