package com.capgemini.piapi;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import com.capgemini.piapi.domain.ProductOwner;
import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.service.ProductOwnerService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment =WebEnvironment.RANDOM_PORT)
class ProductOwnerIntegrationTest {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;
	HttpHeaders headers= new HttpHeaders();
	@Autowired
	ProductOwnerService productOwnerService;
	
	@Test
	void test_handleProductOwnerLogin() {
		Map<String,String> loginBody=new HashMap<>();
		loginBody.put("loginName","Aadesh");
		loginBody.put("pwd","Aadesh");
		HttpEntity<Map<String,String>> entity= new HttpEntity<>(loginBody, headers);
		ResponseEntity<ProductOwner> response= restTemplate.exchange("http://localhost:"+port+"/api/owner/login", HttpMethod.POST, entity, ProductOwner.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Aadesh",response.getBody().getLoginName());
	}

}
