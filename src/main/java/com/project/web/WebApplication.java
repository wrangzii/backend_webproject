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
		String ACCESS_TOKEN = "sl.BFDA-_WbBWDv1SJ3AnRCvKOuWl0gX1VcRJjo51UvMtathZa3PklthfUBIJqfLP1yi1pgVyi7SjuLq7HSCVYLhabP1SA_6ANljHZ71t6W_M8kFQ3rdssgUxSW2WtYn8JPQH6g0HzT-W3E";
		DbxRequestConfig config = new DbxRequestConfig("/");
		return new DbxClientV2(config, ACCESS_TOKEN);
	}
}
