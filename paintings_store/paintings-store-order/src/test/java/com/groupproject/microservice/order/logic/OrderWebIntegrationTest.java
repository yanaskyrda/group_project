package com.groupproject.microservice.order.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Objects;
import java.util.stream.StreamSupport;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.groupproject.microservice.order.PaintingOrderApp;
import com.groupproject.microservice.order.clients.CatalogClient;
import com.groupproject.microservice.order.clients.Customer;
import com.groupproject.microservice.order.clients.CustomerClient;
import com.groupproject.microservice.order.clients.Item;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PaintingOrderApp.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class OrderWebIntegrationTest {

	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${server.port}")
	private long serverPort;

	@Autowired
	private CatalogClient catalogClient;

	@Autowired
	private CustomerClient customerClient;

	@Autowired
	private OrderRepository orderRepository;

	private Item item;

	private Customer customer;

	@Before
	public void setup() {
		item = catalogClient.findAll().iterator().next();
		customer = customerClient.findAll().iterator().next();
		assertEquals("Darik", customer.getFirstname());
	}

	@Test
	public void isOrderListReturned() {
		try {
			Iterable<Order> orders = orderRepository.findAll();
			assertTrue(StreamSupport.stream(orders.spliterator(), false)
					.noneMatch(o -> (o.getCustomerId() == customer.getCustomerId())));
			ResponseEntity<String> resultEntity = restTemplate.getForEntity(orderURL(), String.class);
			assertTrue(resultEntity.getStatusCode().is2xxSuccessful());
			String orderList = resultEntity.getBody();
			assert orderList != null;
			assertFalse(orderList.contains("Darik"));
			Order order = new Order(customer.getCustomerId());
			order.addLine(42, item.getItemId());
			orderRepository.save(order);
			orderList = restTemplate.getForObject(orderURL(), String.class);
			assert orderList != null;
			assertTrue(orderList.contains("Darik"));
		} finally {
			orderRepository.deleteAll();
		}
	}

	private String orderURL() {
		return "http://localhost:" + serverPort;
	}

	@Test
	public void isOrderFormDisplayed() {
		ResponseEntity<String> resultEntity = restTemplate.getForEntity(orderURL() + "/form.html", String.class);
		assertTrue(resultEntity.getStatusCode().is2xxSuccessful());
		assertTrue(Objects.requireNonNull(resultEntity.getBody()).contains("<form"));
	}

	@Test(expected = HttpClientErrorException.MethodNotAllowed.class)
	public void orderFormPostNotAllowed() {
		restTemplate.postForEntity(orderURL() + "/form.html", "42", String.class);
	}

	@Test
	@Transactional
	public void isSubmittedOrderSaved() {
		long before = orderRepository.count();
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("submit", "");
		map.add("customerId", Long.toString(customer.getCustomerId()));
		map.add("orderLine[0].itemId", Long.toString(item.getItemId()));
		map.add("orderLine[0].count", "42");
		restTemplate.postForLocation(orderURL(), map, String.class);
		assertEquals(before + 1, orderRepository.count());
	}
}
