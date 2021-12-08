package com.groupproject.microservice.order.customerstub;

import com.groupproject.microservice.order.clients.Customer;
import org.springframework.context.annotation.Profile;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/customer")
@Profile("test")
public class CustomerStub {

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Customer> getById(@PathVariable("id") long id) {
		if (id != 42) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new Customer(42, "Darik",
				"Iva", "Darik.Iva@gmail.com", "Unnamed street",
				"Kyiv"), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET)
	public PagedModel<Customer> getAll() {
		return new PagedModel<>(Collections.singletonList(new Customer(42, "Darik",
				"Iva", "Darik.Iva@gmail.com", "Unnamed street",
				"Kyiv")), new PageMetadata(1, 0, 1));
	}

}
