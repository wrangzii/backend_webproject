package com.project.web;

import com.project.web.config.WebSecurityConfigForTests;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import({WebSecurityConfigForTests.class})
class WebApplicationTests {

	@Test
	void contextLoads() {
	}

}
