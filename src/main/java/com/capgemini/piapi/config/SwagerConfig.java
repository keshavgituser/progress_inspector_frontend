package com.capgemini.piapi.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwagerConfig {

	@Bean
	public Docket productApi() {
		
		//Configure Swagger and return Docket instace
		return new Docket(DocumentationType.SWAGGER_2)		
				.select().apis(RequestHandlerSelectors.basePackage("com.capgemini.piapi.web"))
				.paths(PathSelectors.regex("/api.*"))				
				.build()
				.apiInfo(metoInfo());
	}

	private ApiInfo metoInfo() {
		// Customize the Swagger output
		ApiInfo apiInfo = new ApiInfo(
				"Personal Project Management API", 
				"PPM API Created by Codegram", 
				"1.0", 
				"Terms of Service", 
				new Contact("Pankaj Sharma", "https://www.codegram.in/", "pankajsimmc@gmail.com"), 
				"CODEGRAM Licence v.1.0", 
				"https://www.codegram.in/", new ArrayList<>());
		return apiInfo;
	}
}