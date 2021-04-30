package com.capgemini.piapi.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;

import com.capgemini.piapi.domain.ProductOwner;
import com.capgemini.piapi.exception.LoginException;
import com.capgemini.piapi.exception.ProductOwnerAlreadyExistException;
import com.capgemini.piapi.exception.ProductOwnerNotFoundException;
import com.capgemini.piapi.repository.ProductOwnerRepository;

class ProductOwnerServiceImplTest {
	//Mock Beans
	@Mock
	MockHttpSession session;
	
	@InjectMocks
	ProductOwnerServiceImpl productOwnerServiceImpl;
	
	//Stubs 
	private ProductOwner productOwner;
	private ProductOwner productOwner1;
	private ProductOwner productOwner2;
	private ProductOwner productOwner3;
	private List<ProductOwner> productOwnerList;
	
	@Mock
	ProductOwnerRepository productOwnerRepository;
	

	@BeforeEach
	public void setup() {	
		MockitoAnnotations.openMocks(this); // invoke mocks
		
		
		productOwnerList=new ArrayList<>();
				
		productOwner1=new ProductOwner("Test Owner", "Test", "Test123");
		productOwner2=new ProductOwner("Test Owner", "Test1", "Test123");
		productOwner3=new ProductOwner("Test Owner", "Test2", "Test123");
		
		productOwnerList.add(productOwner1);
		productOwnerList.add(productOwner2);
		productOwnerList.add(productOwner3);		
		
		productOwner=new ProductOwner();

	}
	//-----------------------------------------------Register Test----------------------------------------------------------
	// REGISTER TEST CASE  :  Successful Registration
	@Test
	void test_saveProductOwner_GivenProductOwner_ShouldReturnSavedProductOwner() {
		BDDMockito.given(productOwnerRepository.save(productOwner1)).willReturn(new ProductOwner("Test Owner", "Test", "Test123"));
		ProductOwner savedProductOwner = productOwnerServiceImpl.saveProductOwner(productOwner1);
		assertEquals(productOwner1.getName(), savedProductOwner.getName());		
		assertEquals(productOwner1.getLoginName(), savedProductOwner.getLoginName());	
		assertEquals(productOwner1.getPwd(), savedProductOwner.getPwd());	
	}	
	
	// REGISTER TEST CASE  :  User Already Exist
	@Test
	void test_saveProductOwner_GivenExistingProductOwner_ShouldThrowProductOwnerAlreadyExistException() {
		BDDMockito.given(productOwnerRepository.findByLoginName(productOwner1.getLoginName())).willReturn(new ProductOwner("Test Owner", "Test", "Test123"));
		BDDMockito.given(productOwnerRepository.save(productOwner1)).willReturn(new ProductOwner("Test Owner", "Test", "Test123"));
		Exception ex=assertThrows(ProductOwnerAlreadyExistException.class, ()->productOwnerServiceImpl.saveProductOwner(productOwner1));
		assertEquals("Product owner already exists !!!", ex.getMessage());
	}	
	
	// REGISTER TEST CASE  :  Empty Fields
	@Test
	void test_saveProductOwner_GivenNull_ShouldThrowProductOwnerNotFoundException() {
		BDDMockito.given(productOwnerRepository.save(productOwner)).willReturn(new ProductOwner("Test Owner", "Test", "Test123"));
		Exception ex=assertThrows(ProductOwnerNotFoundException.class, ()->productOwnerServiceImpl.saveProductOwner(productOwner));
		assertEquals("Please Fill the Required Fields", ex.getMessage());
	
	}	
	//-----------------------------------------------Login Test----------------------------------------------------------
	// LOGIN TEST CASE  :  Successful Login
	@Test
	void test_authenticateProductOwner_GivenProductOwner_ShouldReturnLoggedInProductOwner() {
		BDDMockito.given(productOwnerRepository.findByLoginName(productOwner1.getLoginName())).willReturn(new ProductOwner("Test Owner", "Test", "Test123"));
		BDDMockito.given(session.getAttribute("loginName")).willReturn(productOwner1.getLoginName());
		ProductOwner owner= productOwnerServiceImpl.authenticateProductOwner(productOwner1.getLoginName(),productOwner1.getPwd(), session);
		assertNotNull(owner);
		assertNotNull(owner.getLoginName());
		assertEquals(owner.getLoginName(),session.getAttribute("loginName"));
	}
	
	// LOGIN TEST CASE : Wrong Login Name
	@Test
	void test_authenticateProductOwner_GivenWrongLoginName_ShouldThrowProductOwnerNotFoundException() {
		BDDMockito.given(productOwnerRepository.findByLoginName(productOwner1.getLoginName())).willReturn(new ProductOwner("Test Owner", "Test", "Test123"));
		BDDMockito.given(session.getAttribute("loginName")).willReturn("Test");
		Exception ex=assertThrows(ProductOwnerNotFoundException.class,() -> productOwnerServiceImpl.authenticateProductOwner(productOwner2.getLoginName(),productOwner1.getPwd(), session),"Product Owner with loginName : " + productOwner2.getLoginName() + " does not exist");
		assertEquals("Product Owner with loginName : " + productOwner2.getLoginName() + " does not exist", ex.getMessage());
	}
	// LOGIN TEST CASE : Wrong Password

	@Test
	void test_authenticateProductOwner_GivenWrongPassword_ShouldLoginException() {
		BDDMockito.given(productOwnerRepository.findByLoginName(productOwner1.getLoginName())).willReturn(new ProductOwner("Test Owner", "Test", "Test123"));
		BDDMockito.given(session.getAttribute("loginName")).willReturn(productOwner1.getLoginName());
		Exception ex=assertThrows(LoginException.class,() -> productOwnerServiceImpl.authenticateProductOwner(productOwner1.getLoginName(), "Test1234", session));
		assertEquals("Login Failed ! Invalid Credentials", ex.getMessage());
	}
	
	// LOGIN TEST CASE : Null 

		@Test
		void test_authenticateProductOwner_GivenNull_ShouldLoginException() {
			BDDMockito.given(productOwnerRepository.findByLoginName("Test")).willReturn(new ProductOwner("Test Owner", "Test", "Test123"));
			BDDMockito.given(session.getAttribute("loginName")).willReturn("Test");
			Exception ex=assertThrows(LoginException.class,() -> productOwnerServiceImpl.authenticateProductOwner(productOwner.getLoginName(), productOwner.getPwd(), session));
			assertEquals("Please Enter Credentials", ex.getMessage());
		}
		//-----------------------------------------------View Task Progress Test----------------------------------------------------------
		//-----------------------------------------------Add Client To Task Test----------------------------------------------------------
		//-----------------------------------------------Update Product Owner Test----------------------------------------------------------
		//-----------------------------------------------Delete Product Owner Test----------------------------------------------------------
		//-----------------------------------------------------------------------------------------------------------------------------------------------
	@AfterEach
	public void cleanUp()
	{
		productOwner=null;
		productOwner1=productOwner2=productOwner3=null;
		productOwnerList=null;
	}

}
