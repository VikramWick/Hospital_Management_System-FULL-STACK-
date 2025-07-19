package com.hospital.hms.external;

import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class PharmacyServiceClient {

    @Value("${pharmacy.api.base-url}")
    private String pharmacyApiBaseUrl;

    private final RestTemplate restTemplate;

    public PharmacyServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retry(name = "pharmacyService", fallbackMethod = "getAvailableMedicinesFallback")
    public List<Map<String, Object>> getAvailableMedicines() {
        String url = pharmacyApiBaseUrl + "/medicines";
        return restTemplate.getForObject(url, List.class);
    }

    // Fallback method in case of failure
    public List<Map<String, Object>> getAvailableMedicinesFallback(Throwable throwable) {
        System.err.println("Pharmacy service is unavailable. Returning fallback response.");
        return List.of(Map.of("name", "Fallback Medicine", "price", 0.0));
    }
}
