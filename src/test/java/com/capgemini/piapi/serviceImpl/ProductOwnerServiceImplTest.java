package com.capgemini.piapi.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.capgemini.piapi.domain.ProductOwner;
import com.capgemini.piapi.exception.ProductOwnerAlreadyExistException;
import com.capgemini.piapi.exception.ProductOwnerNotFoundException;
import com.capgemini.piapi.repository.ProductOwnerRepository;
import com.capgemini.piapi.service.ProductOwnerService;

@SpringBootTest
class ProductOwnerServiceImplTest {

	@Autowired
	ProductOwnerService productOwnerService;

	@Autowired
	ProductOwnerRepository productOwnerRepository;

	/*
	 * @Test public void saveProductOwner() {
	 * 
	 * }
	 */

	@Test
	void test_saveProductOwner_GivenProductOwner_ShouldReturnSavedProductOwner() {
		ProductOwner productOwner = new ProductOwner();
		productOwner.setName("Test Owner");
		productOwner.setLoginName("Test");
		productOwner.setPwd("Test123");
		ProductOwner savedProductOwner = productOwnerService.saveProductOwner(productOwner);
		assertEquals(productOwner, savedProductOwner);
	}

	@Test
	void test_saveProductOwner_GivenNull_ShouldThrowProductOwnerNotFoundException() {
		ProductOwner productOwner = null;
		assertThrows(ProductOwnerNotFoundException.class, () -> productOwnerService.saveProductOwner(productOwner));
	}

	@Test
	void test_deleteProductOwnerByLoginName_GivenLoginName_ShouldDeleteProductOwner() {
		productOwnerService.deleteProductOwnerByLoginName("Test");
		assertEquals(productOwnerService.findProductOwnerByLoginName("Test"), null);
	}

}
