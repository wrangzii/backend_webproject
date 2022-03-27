package com.project.web;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.project.web.config.WebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({WebConfig.class})
public class WebApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}
	@Bean("dropboxClient")
	public DbxClientV2 dropboxClient() {
		String ACCESS_TOKEN = "sl.BElsuosunxNTIoajTcMOJfx4DAb5ioiCvYKG3KSA0sdZZYc3JP0gURecPw2KSb4zAIkbnIogMxucqet3fHiygqBJTI1lPZ5w7bOXdTd0qxKAVswb4KNs03Q5dl4jo2ZfhybMY5kH";
		DbxRequestConfig config = new DbxRequestConfig("/");
		return new DbxClientV2(config, ACCESS_TOKEN);
	}
}
