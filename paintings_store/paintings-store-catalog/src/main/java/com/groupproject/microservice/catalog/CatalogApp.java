package com.groupproject.microservice.catalog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@ComponentScan
@EnableAutoConfiguration
@EnableDiscoveryClient
@Component
public class CatalogApp {

	private final ItemRepository itemRepository;

	@Autowired
	public CatalogApp(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@PostConstruct
	public void generateTestData() {
		itemRepository.save(new Item("Mona Lisa", 100.0));
		itemRepository.save(new Item("The Starry Night", 40.0));
		itemRepository.save(new Item("The Last Supper", 150.0));
		itemRepository.save(new Item("The Night Watch", 70.0));
	}

	public static void main(String[] args) {
		SpringApplication.run(CatalogApp.class, args);
	}

}
