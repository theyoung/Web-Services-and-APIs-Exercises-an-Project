package com.udacity.boogle.maps.web;

import com.udacity.boogle.maps.MapsController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.security.SecureRandom;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(MapsController.class)
public class MapsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAddress() throws Exception{
        Double lat = SecureRandom.getInstance("SHA1PRNG").nextDouble() * 100;
        Double lon = SecureRandom.getInstance("SHA1PRNG").nextDouble() * 100;

        String request = String.format("/maps?lat=%2.1f&lon=%2.1f", lat, lon).toString();
        System.out.println(request);

        mockMvc.perform(get(request))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.zip").exists())
                .andExpect(jsonPath("$.city").exists())
                .andExpect(jsonPath("$.state").exists());
    }

}
