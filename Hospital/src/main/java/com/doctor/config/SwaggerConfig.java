package com.doctor.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableWebMvc
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(getInfo()).select()
				.paths(PathSelectors.any())
				.apis(RequestHandlerSelectors.basePackage("com.doctor"))
				.build();
	}
	
	private ApiInfo getInfo() {
		return new ApiInfo("Hospital Backend", "This Project Devloped For Hospital Mangement", "1.0", "Terms of Service", new Contact("Mihir", "https://google", 			"mihirthumaraaa@gmail.com"), "Licens of Apis", "API licens URL", Collections.emptyList());
	}
	
}
