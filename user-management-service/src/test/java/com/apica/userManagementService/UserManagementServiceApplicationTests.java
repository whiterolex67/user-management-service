package com.apica.userManagementService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:testdb",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.hibernate.ddl-auto=none",
		"spring.kafka.bootstrap-servers=localhost:9092" // dummy value
})
class UserManagementServiceApplicationTests {

	@Test
	void contextLoads() {
	}
}
