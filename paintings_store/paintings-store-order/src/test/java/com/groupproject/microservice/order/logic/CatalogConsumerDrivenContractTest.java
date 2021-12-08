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
import com.groupproject.microservice.order.clients.CatalogClient;
import com.groupproject.microservice.order.clients.PaintingItem;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PaintingOrderApp.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class CatalogConsumerDrivenContractTest {

	@Autowired
	CatalogClient catalogClient;

	@Test
	public void testFindAll() {
		Collection<PaintingItem> result = catalogClient.findAll();
		assertEquals(1, result.stream()
				.filter(i -> (i.getName().equals("lenovoTablet") && i.getPrice() == 42.0 && i.getItemId() == 1)).count());
	}

	@Test
	public void testGetOne() {
		Collection<PaintingItem> allPaintingItems = catalogClient.findAll();
		long id = allPaintingItems.iterator().next().getItemId();
		PaintingItem result = catalogClient.getOne(id);
		assertEquals(id, result.getItemId());
	}

}
