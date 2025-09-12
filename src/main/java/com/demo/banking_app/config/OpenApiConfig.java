package com.demo.banking_app.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public GroupedOpenApi v1Api() {
		return GroupedOpenApi.builder()
				.group("v1")
				.pathsToMatch("/api/v1/**")
				.build();
	}
}


