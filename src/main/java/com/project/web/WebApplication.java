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
		String ACCESS_TOKEN = "sl.BFmQvKtZXPBfAQZFtlnGJeOn8rg4RcF6OuPRVTKDotjURYG9QwIT8non2axhu_--dmdWV_Dn3AFVEoPHZ0Q-mc5H0yzvaWoG-ZO5IaqNDZ3ehVseH6pR6PHVY00a-CRxpNw-7GyJpIWq";
		DbxRequestConfig config = new DbxRequestConfig("/");
		return new DbxClientV2(config, ACCESS_TOKEN);
	}
}
