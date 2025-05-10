//BEGIN: Work/DemoTestCreator/2025-04-16__14-59-55.151__GenerateTests/Input/Existing_Tests/VulnadoApplicationTests.java
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
		// Ensures the application context loads successfully
	}

	@Test
	public void main_ShouldSetupPostgresAndRunApplication() {
		// Arrange
		String[] args = {};
		Postgres postgresMock = mock(Postgres.class);
		SpringApplication springApplicationMock = mock(SpringApplication.class);

		// Act
		VulnadoApplication.main(args);

		// Assert
		verify(postgresMock, times(1)).setup();
		verify(springApplicationMock, times(1)).run(VulnadoApplication.class, args);
	}
}
//END: Work/DemoTestCreator/2025-04-16__14-59-55.151__GenerateTests/Input/Existing_Tests/VulnadoApplicationTests.java
