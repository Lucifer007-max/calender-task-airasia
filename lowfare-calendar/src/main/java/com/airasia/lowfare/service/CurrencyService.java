package com.airasia.lowfare.service;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class CurrencyService {
    private static final Map<String, Double> exchangeRates = Map.of(
            "USD", 1.0,
            "MYR", 3.97,
            "THB", 32.71
    );  
    public  Integer convert ( Integer amount, String currency) {
        if(!exchangeRates.containsKey(currency)){
            throw new RuntimeException("Unsupported currency");

        }
        Double rate = exchangeRates.getOrDefault(currency, 1.0);
        return (int) Math.round(amount * rate); 
    }
}
