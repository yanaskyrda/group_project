package com.groupproject.microservice.customer;

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
public class CustomerApp {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerApp(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostConstruct
    public void generateTestData() {
        customerRepository.save(new Customer("Yana", "Skirda",
                "yana.skyrda@gmail.com", "Vozrojdenia", "Kyiv"));
        customerRepository.save(new Customer("Darik", "Ivashin",
                "darik.ivashin@gmail.com", "Radyjnaya", "Kyiv"));
        customerRepository.save(new Customer("Sanya", "Totskiy",
                "sanya.totskiy@gmail.com", "Bogdanoskaya", "Kyiv"));
        customerRepository.save(new Customer("Anton", "Gladkiy",
                "anton.gladkiy@gmail.com", "Cherkasovskaya", "Kyiv"));
    }

    public static void main(String[] args) {
        SpringApplication.run(CustomerApp.class, args);
    }

}

