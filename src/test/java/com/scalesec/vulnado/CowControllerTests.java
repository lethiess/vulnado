package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mockStatic;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CowControllerTests {

    @InjectMocks
    private CowController cowController;

    // Test to verify the default input value
    @Test
    public void cowsay_DefaultInput_ShouldReturnDefaultMessage() {
        try (MockedStatic<Cowsay> mockedCowsay = mockStatic(Cowsay.class)) {
            // Mocking the Cowsay.run method
            mockedCowsay.when(() -> Cowsay.run("I love Linux!")).thenReturn("Mocked Cowsay Output");

            String result = cowController.cowsay(null);

            assertEquals("Expected mocked output for default input", "Mocked Cowsay Output", result);
        }
    }

    // Test to verify custom input value
    @Test
    public void cowsay_CustomInput_ShouldReturnCustomMessage() {
        try (MockedStatic<Cowsay> mockedCowsay = mockStatic(Cowsay.class)) {
            // Mocking the Cowsay.run method
            mockedCowsay.when(() -> Cowsay.run("Hello, World!")).thenReturn("Mocked Cowsay Output for Hello, World!");

            String result = cowController.cowsay("Hello, World!");

            assertEquals("Expected mocked output for custom input", "Mocked Cowsay Output for Hello, World!", result);
        }
    }

    // Test to verify empty input value
    @Test
    public void cowsay_EmptyInput_ShouldReturnEmptyMessage() {
        try (MockedStatic<Cowsay> mockedCowsay = mockStatic(Cowsay.class)) {
            // Mocking the Cowsay.run method
            mockedCowsay.when(() -> Cowsay.run("")).thenReturn("Mocked Cowsay Output for Empty Input");

            String result = cowController.cowsay("");

            assertEquals("Expected mocked output for empty input", "Mocked Cowsay Output for Empty Input", result);
        }
    }
}
