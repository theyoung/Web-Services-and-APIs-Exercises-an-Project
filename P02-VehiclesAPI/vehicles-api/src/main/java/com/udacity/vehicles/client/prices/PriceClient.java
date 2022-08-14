package com.udacity.vehicles.client.prices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.jayway.jsonpath.*;

/**
 * Implements a class to interface with the Pricing Client for price data.
 *
 * PriceClient
 * Handles the format of a GET request to the pricing-service WebClient to get pricing data.
 *
 */
@Component
public class PriceClient {

    private static final Logger log = LoggerFactory.getLogger(PriceClient.class);

    private final WebClient client;

    public PriceClient(WebClient pricing) {
        this.client = pricing;
    }

    // In a real-world application we'll want to add some resilience
    // to this method with retries/CB/failover capabilities
    // We may also want to cache the results so we don't need to
    // do a request every time
    /**
     * Gets a vehicle price from the pricing client, given vehicle ID.
     * @param vehicleId ID number of the vehicle for which to get the price
     * @return Currency and price of the requested vehicle,
     *   error message that the vehicle ID is invalid, or note that the
     *   service is down.
     */
    public Price getPrice(Long vehicleId) {
        Price price = new Price();
        try {
            String response = client
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("prices/search/findByVehicleId")
                            .queryParam("vehicleId", vehicleId)
                            .build()
                    )
                    .retrieve().bodyToMono(String.class).block();

            DocumentContext context = JsonPath.parse(response);
            List<Map> prices = context.read("$._embedded.prices[*]");

            price.setPrice(BigDecimal.valueOf(Double.parseDouble(prices.get(0).get("price").toString())));
            price.setCurrency(prices.get(0).get("currency").toString());

            return price;
        } catch (Exception e) {
            log.error("Unexpected error retrieving price for vehicle {}", vehicleId, e);
        }
        return price;
    }
}
