package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

	@Test
	public void contextLoads() {
		// Ensures the application context loads successfully
	}

	@Test
	public void main_ShouldInvokePostgresSetupAndRunApplication() {
		// Arrange
		Mockito.mockStatic(Postgres.class); // Mocking Postgres class
		Mockito.mockStatic(SpringApplication.class); // Mocking SpringApplication class

		String[] args = new String[] { "arg1", "arg2" };

		// Act
		VulnadoApplication.main(args);

		// Assert
		Mockito.verify(Postgres.class, Mockito.times(1)).setup(); // Verifies Postgres.setup() is called once
		Mockito.verify(SpringApplication.class, Mockito.times(1)).run(VulnadoApplication.class, args); // Verifies SpringApplication.run() is called with correct arguments
	}
}
