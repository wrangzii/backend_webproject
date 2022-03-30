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
		String ACCESS_TOKEN = "sl.BEy_t6ZewuP3euvIhzvV8AJ_OVz9Ena1FZaktGvENdIQJXR678OEbGKT0_ZWzU9ZyISrjfE3kvcvoKo_ZFFrylBAo8mdLlDjVlxpY4a6cF4V10a5Dh0OP69Ty31TaHrF8m5rPASq";
		DbxRequestConfig config = new DbxRequestConfig("/");
		return new DbxClientV2(config, ACCESS_TOKEN);
	}
}
