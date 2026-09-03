package lk.sunrise.billing;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.sunrise.billing.controller.BillingApiController;
import lk.sunrise.billing.dto.BillingRequest;
import lk.sunrise.billing.dto.BillingResponse;
import lk.sunrise.billing.service.BillingCalculationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BillingApiController.class)
class BillingApiControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean BillingCalculationService service;

    @Test
    void shouldReturnCalculatedQuoteFromRestEndpoint() throws Exception {
        when(service.calculate(any(BillingRequest.class))).thenReturn(new BillingResponse(
                new BigDecimal("5000.00"), new BigDecimal("2000.00"), new BigDecimal("7000.00")));

        mockMvc.perform(post("/api/billing/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new BillingRequest(
                                new BigDecimal("5000.00"), new BigDecimal("2000.00")))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(7000.00));
    }

    @Test
    void shouldRejectNegativeBillingValues() throws Exception {
        mockMvc.perform(post("/api/billing/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"treatmentCost\":-1,\"consultationFee\":1000}"))
                .andExpect(status().isBadRequest());
    }
}
