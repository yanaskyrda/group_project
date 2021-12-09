package com.groupproject.microservice.customer;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "customer", path = "customer")
public interface CustomerRepository extends
        PagingAndSortingRepository<Customer, Long> {

    List<Customer> findBySurname(@Param("surname") String surname);

}

