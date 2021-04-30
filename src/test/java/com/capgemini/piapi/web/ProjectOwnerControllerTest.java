package com.capgemini.piapi.web;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.capgemini.piapi.service.ProductOwnerService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProjectOwnerControllerTest.class)
class ProjectOwnerControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	ProductOwnerService projectOwnerService;
	
	@Test
	void test_handleProductOwnerLogin() {
		
	}

}
