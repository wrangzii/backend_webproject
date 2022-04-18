package com.project.web;

import com.project.web.config.WebSecurityConfigForTests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import({WebSecurityConfigForTests.class})
class WebApplicationTests {

	@Test
	void contextLoads() {
		Assertions.assertDoesNotThrow(this::doNotThrowException);
	}
	private void doNotThrowException(){
		//This method will never throw exception
	}

}
