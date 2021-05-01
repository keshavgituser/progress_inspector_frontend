package com.capgemini.piapi.serviceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;

import com.capgemini.piapi.domain.ProductOwner;
import com.capgemini.piapi.exception.ProductOwnerAlreadyExistException;
import com.capgemini.piapi.exception.ProductOwnerNotFoundException;
import com.capgemini.piapi.repository.ProductOwnerRepository;
import com.capgemini.piapi.service.ProductOwnerService;

@SpringBootTest
class ProductOwnerServiceImplTest {
	@Autowired
	MockHttpSession session;
	
	@Autowired
	ProductOwnerService productOwnerService;

	@Autowired
	ProductOwnerRepository productOwnerRepository;

	//
	
	@Test
	void test_saveProductOwner_GivenProductOwner_ShouldReturnSavedProductOwner() {
		ProductOwner productOwner = new ProductOwner();
		productOwner.setName("Test Owner");
		productOwner.setLoginName("Test");
		productOwner.setPwd("Test123");
		ProductOwner savedProductOwner = productOwnerService.saveProductOwner(productOwner);
		assertEquals(productOwner, savedProductOwner);		
		productOwnerRepository.delete(productOwner);
	}
	
	@Test
	void test_saveProductOwner_GivenExistingProductOwner_ShouldReturnSavedProductOwner() {
		ProductOwner productOwner = new ProductOwner();
		productOwner.setName("Test Owner");
		productOwner.setLoginName("Test");
		productOwner.setPwd("Test123");
		ProductOwner savedProductOwner = productOwnerService.saveProductOwner(productOwner);
		
		assertThrows(ProductOwnerAlreadyExistException.class, () -> productOwnerService.saveProductOwner(productOwner));		
		
		productOwnerRepository.delete(productOwner);
	}

	@Test
	void test_saveProductOwner_GivenNull_ShouldThrowProductOwnerNotFoundException() {
		ProductOwner productOwner = new ProductOwner();
		assertThrows(ProductOwnerNotFoundException.class, () -> productOwnerService.saveProductOwner(productOwner));
	}

	@Test
	void test_deleteProductOwnerByLoginName_GivenLoginName_ShouldDeleteProductOwner() {
		ProductOwner productOwner = new ProductOwner();
		productOwner.setName("Test Owner");
		productOwner.setLoginName("Test");
		productOwner.setPwd("Test123");
		productOwnerService.saveProductOwner(productOwner);
		productOwnerService.deleteProductOwnerByLoginName(productOwner.getLoginName());
		assertEquals(productOwnerService.findProductOwnerByLoginName("Test"), null);
	}
	
	@Test
	void test_authenticateProductOwner_GivenProductOwner_ShouldReturnLoggedInProductOwner() {
		ProductOwner productOwner = new ProductOwner();
		productOwner.setName("Test Owner");
		productOwner.setLoginName("Test123");
		productOwner.setPwd("Test123");
		ProductOwner savedProductOwner = productOwnerService.saveProductOwner(productOwner); 
		productOwnerService.authenticateProductOwner(savedProductOwner.getLoginName(), savedProductOwner.getPwd(), session);
		assertEquals(productOwner.getLoginName(),session.getAttribute("loginName"));
		productOwnerRepository.delete(productOwner);

	}
	@Test
	void test_authenticateProductOwner_GivenWrongProductOwner_ShouldThrowProductOwnerAlreadyExistException() {
		ProductOwner productOwner = new ProductOwner();
		productOwner.setName("Test Owner1");
		productOwner.setLoginName("Test1");
		productOwner.setPwd("Test1234");
		assertThrows(ProductOwnerNotFoundException.class, 
				() -> productOwnerService.authenticateProductOwner(productOwner.getLoginName(), 
						productOwner.getPwd(), session));
	}

}
