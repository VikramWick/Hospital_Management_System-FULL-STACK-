package com.hospital.hms.external;

import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.Map;

@Service
public class InsuranceServiceClient {

    @Value("${insurance.api.base-url}")
    private String insuranceApiBaseUrl;

    private final RestTemplate restTemplate;

    public InsuranceServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retry(name = "insuranceService", fallbackMethod = "getInsurancePlansFallback")
    public List<Map<String, Object>> getInsurancePlans() {
        String url = insuranceApiBaseUrl + "/plans";
        return restTemplate.getForObject(url, List.class);
    }

    // Fallback method in case of failure
    public List<Map<String, Object>> getInsurancePlansFallback(Throwable throwable) {
        System.err.println("Insurance service is unavailable. Returning fallback response.");
        return List.of(Map.of("name", "Fallback Plan", "premium", 0.0));
    }
}
