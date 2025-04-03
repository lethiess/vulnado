package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

	@Test
	public void contextLoads() {
		// Test to ensure the application context loads successfully
	}

	@Test
	public void main_ShouldInitializeApplication() {
		// Mocking Postgres.setup() to ensure it is called
		Postgres mockPostgres = Mockito.mock(Postgres.class);
		Mockito.doNothing().when(mockPostgres).setup();

		// Mocking SpringApplication.run() to ensure it is called
		SpringApplication mockSpringApplication = Mockito.mock(SpringApplication.class);
		Mockito.doNothing().when(mockSpringApplication).run(Mockito.any(Class.class), Mockito.any(String[].class));

		// Calling the main method
		VulnadoApplication.main(new String[]{});

		// Verifying that Postgres.setup() was called
		Mockito.verify(mockPostgres, Mockito.times(1)).setup();

		// Verifying that SpringApplication.run() was called
		Mockito.verify(mockSpringApplication, Mockito.times(1)).run(Mockito.any(Class.class), Mockito.any(String[].class));
	}
}
