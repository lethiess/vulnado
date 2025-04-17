package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CowControllerTests {

    @Autowired
    private MockMvc mockMvc;

    // Test to verify the default input value
    @Test
    public void cowsay_DefaultInput_ShouldReturnDefaultMessage() throws Exception {
        mockMvc.perform(get("/cowsay"))
                .andExpect(status().isOk())
                .andExpect(content().string(Cowsay.run("I love Linux!")));
    }

    // Test to verify custom input value
    @Test
    public void cowsay_CustomInput_ShouldReturnCustomMessage() throws Exception {
        String customInput = "Hello, World!";
        mockMvc.perform(get("/cowsay").param("input", customInput))
                .andExpect(status().isOk())
                .andExpect(content().string(Cowsay.run(customInput)));
    }

    // Test to verify empty input value
    @Test
    public void cowsay_EmptyInput_ShouldReturnDefaultMessage() throws Exception {
        mockMvc.perform(get("/cowsay").param("input", ""))
                .andExpect(status().isOk())
                .andExpect(content().string(Cowsay.run("I love Linux!")));
    }

    // Test to verify null input value
    @Test
    public void cowsay_NullInput_ShouldReturnDefaultMessage() throws Exception {
        mockMvc.perform(get("/cowsay").param("input", (String) null))
                .andExpect(status().isOk())
                .andExpect(content().string(Cowsay.run("I love Linux!")));
    }

    // Test to verify special characters in input
    @Test
    public void cowsay_SpecialCharactersInput_ShouldReturnSpecialCharactersMessage() throws Exception {
        String specialInput = "!@#$%^&*()_+";
        mockMvc.perform(get("/cowsay").param("input", specialInput))
                .andExpect(status().isOk())
                .andExpect(content().string(Cowsay.run(specialInput)));
    }

    // Test to verify long input value
    @Test
    public void cowsay_LongInput_ShouldReturnLongMessage() throws Exception {
        String longInput = "a".repeat(1000);
        mockMvc.perform(get("/cowsay").param("input", longInput))
                .andExpect(status().isOk())
                .andExpect(content().string(Cowsay.run(longInput)));
    }
}
