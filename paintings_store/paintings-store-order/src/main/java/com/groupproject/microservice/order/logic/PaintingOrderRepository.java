package com.groupproject.microservice.order.logic;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "order", path = "order")
interface PaintingOrderRepository extends PagingAndSortingRepository<PaintingOrder, Long> {

}
