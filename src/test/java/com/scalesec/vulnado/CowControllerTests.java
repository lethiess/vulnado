package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CowControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Cowsay cowsayMock;

    // Test to verify the default behavior of the /cowsay endpoint
    @Test
    public void cowsay_DefaultInput_ShouldReturnDefaultMessage() throws Exception {
        String defaultMessage = "I love Linux!";
        String expectedOutput = "Mocked Cowsay Output for Default Message";

        // Mocking the Cowsay.run method
        Mockito.when(cowsayMock.run(defaultMessage)).thenReturn(expectedOutput);

        mockMvc.perform(get("/cowsay"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedOutput));
    }

    // Test to verify the /cowsay endpoint with a custom input
    @Test
    public void cowsay_CustomInput_ShouldReturnCustomMessage() throws Exception {
        String customMessage = "Hello, World!";
        String expectedOutput = "Mocked Cowsay Output for Custom Message";

        // Mocking the Cowsay.run method
        Mockito.when(cowsayMock.run(customMessage)).thenReturn(expectedOutput);

        mockMvc.perform(get("/cowsay").param("input", customMessage))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedOutput));
    }

    // Test to verify the /cowsay endpoint with an empty input
    @Test
    public void cowsay_EmptyInput_ShouldReturnDefaultMessage() throws Exception {
        String defaultMessage = "I love Linux!";
        String expectedOutput = "Mocked Cowsay Output for Default Message";

        // Mocking the Cowsay.run method
        Mockito.when(cowsayMock.run(defaultMessage)).thenReturn(expectedOutput);

        mockMvc.perform(get("/cowsay").param("input", ""))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedOutput));
    }

    // Test to verify the /cowsay endpoint with a null input
    @Test
    public void cowsay_NullInput_ShouldReturnDefaultMessage() throws Exception {
        String defaultMessage = "I love Linux!";
        String expectedOutput = "Mocked Cowsay Output for Default Message";

        // Mocking the Cowsay.run method
        Mockito.when(cowsayMock.run(defaultMessage)).thenReturn(expectedOutput);

        mockMvc.perform(get("/cowsay").param("input", (String) null))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedOutput));
    }
}
