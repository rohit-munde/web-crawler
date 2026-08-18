package com.backend.web_crawler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebCrawlerApplicationTests {

	@Test
	void contextLoads() {
		Assertions.assertThat(this.getClass().getName()).isNotEmpty();
	}

}
