package com.udacity.pricing.rest;

import com.udacity.pricing.domain.price.Price;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.json.*;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jayway.jsonpath.*;
import org.springframework.web.reactive.function.client.WebClient;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_UTF8;


@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"eureka.client.enabled=false","eureka.client.register-with-eureka=false"}
)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class PricingRestTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getVehiclePriceTest() throws JSONException {
        ResponseEntity<String> response = this.restTemplate.getForEntity("http://localhost:"+ port +"/prices/search/findByVehicleId?vehicleId=22", String.class);
        String body = response.getBody();
        DocumentContext context = JsonPath.parse(body);
        List<Map> prices = context.read("$._embedded.prices[*]");
        List<Price> priceList = new ArrayList<>();

        for(Map price : prices){
            System.out.println(price);
            Price cur = new Price();
            cur.setCurrency(String.valueOf(price.get("currency")));
            cur.setPrice(BigDecimal.valueOf((double) price.get("price")));
            cur.setVehicleId(Long.valueOf((Integer) price.get("vehicleId")));
            priceList.add(cur);
        }
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        System.out.println("Content Type = " + response.getHeaders().getContentType());
//        assertThat(response.getHeaders().getContentType(), equalTo(HAL_JSON_UTF8));
        assertThat(priceList.size(), equalTo(1));
        assertThat(priceList.get(0).getCurrency(), equalTo("USD"));
        assertThat(priceList.get(0).getVehicleId(), equalTo(22L));
        assertThat(priceList.get(0).getPrice().toString(), equalTo("20000.0"));

    }

//    @Test
//    public void traversonTest() throws URISyntaxException {
//        Object obj = WebClient.create("http://localhost:"+ port +"/prices/search/findByVehicleId?vehicleId=22").get().exchange().block();
//        System.out.println(obj.toString());
//    }
}
