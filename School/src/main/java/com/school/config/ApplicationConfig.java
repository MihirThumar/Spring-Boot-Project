package com.school.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "spring")
@PropertySource(value = "file:/C:/Users/mihir/OneDrive/Desktop/Data/Properties/propertie.yml" , factory = MyProperties.class)
@Data
public class ApplicationConfig {
		
	private Map<String, String> datasource;
	private Map<String, String> jpa;
	
}
