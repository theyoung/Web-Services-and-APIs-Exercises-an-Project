package com.udacity.vehicles.api;

import com.udacity.vehicles.client.prices.Price;
import com.udacity.vehicles.client.prices.PriceClient;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CarFeaturesTest {

    @Autowired
    PriceClient client;

    @Test
    public void test(){
        Price price = client.getPrice(21L);

        Assertions.assertThat(price.getCurrency()).isEqualTo("USD");
        Assertions.assertThat(price.getPrice()).isEqualTo( new BigDecimal("10000.0"));
    }
}
