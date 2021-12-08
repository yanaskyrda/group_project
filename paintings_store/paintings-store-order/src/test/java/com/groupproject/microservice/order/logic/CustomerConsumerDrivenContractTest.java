package com.groupproject.microservice.order.logic;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.groupproject.microservice.order.PaintingOrderApp;
import com.groupproject.microservice.order.clients.Customer;
import com.groupproject.microservice.order.clients.CustomerClient;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PaintingOrderApp.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class CustomerConsumerDrivenContractTest {

	@Autowired
	CustomerClient customerClient;

	@Test
	public void testFindAll() {
		Collection<Customer> result = customerClient.findAll();
		assertEquals(1,
				result.stream()
						.filter(c -> (c.getSurname().equals("Iva") && c.getFirstname().equals("Darik")
								&& c.getEmail().equals("Darik.Iva@gmail.com")
								&& c.getAddress().equals("Unnamed street") && c.getCity().equals("Kyiv")))
						.count());
	}

	@Test
	public void testGetOne() {
		Collection<Customer> allCustomer = customerClient.findAll();
		long id = allCustomer.iterator().next().getCustomerId();
		Customer result = customerClient.getOne(id);
		assertEquals(id, result.getCustomerId());
	}

	@Test
	public void testValidCustomerId() {
		Collection<Customer> allCustomer = customerClient.findAll();
		long id = allCustomer.iterator().next().getCustomerId();
		assertTrue(customerClient.isValidCustomerId(id));
		assertFalse(customerClient.isValidCustomerId(-1));
	}

}
