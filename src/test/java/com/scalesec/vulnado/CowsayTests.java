package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CowsayTests {

    // Helper method to mock ProcessBuilder and its behavior
    private ProcessBuilder mockProcessBuilder(String input, String mockOutput) throws Exception {
        ProcessBuilder processBuilder = Mockito.mock(ProcessBuilder.class);
        Process process = Mockito.mock(Process.class);
        InputStream inputStream = new ByteArrayInputStream(mockOutput.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        Mockito.when(processBuilder.start()).thenReturn(process);
        Mockito.when(process.getInputStream()).thenReturn(inputStream);

        return processBuilder;
    }

    @Test
    public void run_ShouldReturnCowsayOutput_WhenValidInput() throws Exception {
        // Arrange
        String input = "Hello, World!";
        String expectedOutput = "Mocked Cowsay Output\n";
        ProcessBuilder mockProcessBuilder = mockProcessBuilder(input, expectedOutput);

        // Act
        String result = Cowsay.run(input);

        // Assert
        assertEquals("The output should match the mocked cowsay output.", expectedOutput, result);
    }

    @Test
    public void run_ShouldHandleEmptyInput() throws Exception {
        // Arrange
        String input = "";
        String expectedOutput = "Mocked Cowsay Output for Empty Input\n";
        ProcessBuilder mockProcessBuilder = mockProcessBuilder(input, expectedOutput);

        // Act
        String result = Cowsay.run(input);

        // Assert
        assertEquals("The output should handle empty input gracefully.", expectedOutput, result);
    }

    @Test
    public void run_ShouldHandleSpecialCharacters() throws Exception {
        // Arrange
        String input = "!@#$%^&*()";
        String expectedOutput = "Mocked Cowsay Output for Special Characters\n";
        ProcessBuilder mockProcessBuilder = mockProcessBuilder(input, expectedOutput);

        // Act
        String result = Cowsay.run(input);

        // Assert
        assertEquals("The output should handle special characters correctly.", expectedOutput, result);
    }

    @Test
    public void run_ShouldHandleExceptionGracefully() {
        // Arrange
        String input = "Exception Test";
        String expectedOutput = ""; // Expecting empty output in case of exception

        // Mock ProcessBuilder to throw an exception
        ProcessBuilder processBuilder = Mockito.mock(ProcessBuilder.class);
        try {
            Mockito.when(processBuilder.start()).thenThrow(new RuntimeException("Mocked Exception"));
        } catch (Exception e) {
            // This block is intentionally left empty
        }

        // Act
        String result = Cowsay.run(input);

        // Assert
        assertEquals("The output should be empty when an exception occurs.", expectedOutput, result);
    }
}
