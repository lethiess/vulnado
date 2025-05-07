//BEGIN: Work/DemoTestCreator/2025-05-07__13-50-59.097__GenerateTests/Input/Existing_Tests/VulnadoApplicationTests.java
package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

	@Test
	public void contextLoads() {
		// Test to ensure the application context loads successfully
	}

	@Test
	public void main_ShouldSetupPostgresAndRunApplication() {
		// Arrange
		Postgres mockPostgres = mock(Postgres.class);
		SpringApplication mockSpringApplication = mock(SpringApplication.class);

		// Act
		VulnadoApplication.main(new String[]{});

		// Assert
		verify(mockPostgres, times(1)).setup();
		verify(mockSpringApplication, times(1)).run(VulnadoApplication.class, new String[]{});
	}
}
//END: Work/DemoTestCreator/2025-05-07__13-50-59.097__GenerateTests/Input/Existing_Tests/VulnadoApplicationTests.java
