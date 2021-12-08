package com.groupproject.microservice.order.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groupproject.microservice.order.clients.CatalogClient;
import com.groupproject.microservice.order.clients.CustomerClient;

@Service
class PaintingOrderService {

	private PaintingOrderRepository paintingOrderRepository;
	private CustomerClient customerClient;
	private CatalogClient itemClient;

	@Autowired
	private PaintingOrderService(PaintingOrderRepository paintingOrderRepository,
								 CustomerClient customerClient, CatalogClient itemClient) {
		super();
		this.paintingOrderRepository = paintingOrderRepository;
		this.customerClient = customerClient;
		this.itemClient = itemClient;
	}

	public PaintingOrder order(PaintingOrder paintingOrder) {
		if (paintingOrder.getNumberOfLines() == 0) {
			throw new IllegalArgumentException("No order lines!");
		}
		if (!customerClient.isValidCustomerId(paintingOrder.getCustomerId())) {
			throw new IllegalArgumentException("Customer does not exist!");
		}
		return paintingOrderRepository.save(paintingOrder);
	}

	public double getPrice(long orderId) {
		return paintingOrderRepository.findById(orderId).get().totalPrice(itemClient);
	}

}
