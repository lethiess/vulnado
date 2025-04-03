package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VulnadoApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
    }

    // Test for default input
    @Test
    public void cowsay_DefaultInput_ShouldReturnDefaultMessage() {
        ResponseEntity<String> response = restTemplate.getForEntity("/cowsay", String.class);
        assertNotNull("Response should not be null", response);
        assertEquals("Response status should be 200", 200, response.getStatusCodeValue());
        assertEquals("Response body should match default message", Cowsay.run("I love Linux!"), response.getBody());
    }

    // Test for custom input
    @Test
    public void cowsay_CustomInput_ShouldReturnCustomMessage() {
        String customMessage = "Hello, World!";
        ResponseEntity<String> response = restTemplate.getForEntity("/cowsay?input=" + customMessage, String.class);
        assertNotNull("Response should not be null", response);
        assertEquals("Response status should be 200", 200, response.getStatusCodeValue());
        assertEquals("Response body should match custom message", Cowsay.run(customMessage), response.getBody());
    }

    // Test for empty input
    @Test
    public void cowsay_EmptyInput_ShouldReturnDefaultMessage() {
        ResponseEntity<String> response = restTemplate.getForEntity("/cowsay?input=", String.class);
        assertNotNull("Response should not be null", response);
        assertEquals("Response status should be 200", 200, response.getStatusCodeValue());
        assertEquals("Response body should match default message", Cowsay.run("I love Linux!"), response.getBody());
    }
}
