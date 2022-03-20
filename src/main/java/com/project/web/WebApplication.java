package com.project.web;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.project.web.config.WebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({WebConfig.class})
public class WebApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}
	@Bean("dropboxClient")
	public DbxClientV2 dropboxClient() {
		String ACCESS_TOKEN = "sl.BEKnUCX_C_R6gkUjXEj3FP_8MRhAtxhETF3Rna32mSP7hxWsvVpZlkTdjpuMYlebcnGoqxRIKGAHgrsmlY02j5VuOI_pXq-upVataedZHJXrUzwyatqYsWxClMPo8Yl9l8JTTfxn4cQ";
		DbxRequestConfig config = new DbxRequestConfig("dropbox/test");
		return new DbxClientV2(config, ACCESS_TOKEN);
	}
}
