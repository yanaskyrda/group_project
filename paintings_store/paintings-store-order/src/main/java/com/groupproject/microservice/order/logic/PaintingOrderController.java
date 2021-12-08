package com.groupproject.microservice.order.logic;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.groupproject.microservice.order.clients.CatalogClient;
import com.groupproject.microservice.order.clients.Customer;
import com.groupproject.microservice.order.clients.CustomerClient;
import com.groupproject.microservice.order.clients.PaintingItem;

@Controller
class PaintingOrderController {

	private PaintingOrderRepository paintingOrderRepository;

	private PaintingOrderService paintingOrderService;

	private CustomerClient customerClient;
	private CatalogClient catalogClient;

	@Autowired
	private PaintingOrderController(PaintingOrderService paintingOrderService, PaintingOrderRepository paintingOrderRepository, CustomerClient customerClient,
									CatalogClient catalogClient) {
		super();
		this.paintingOrderRepository = paintingOrderRepository;
		this.customerClient = customerClient;
		this.catalogClient = catalogClient;
		this.paintingOrderService = paintingOrderService;
	}

	@ModelAttribute("items")
	public Collection<PaintingItem> items() {
		return catalogClient.findAll();
	}

	@ModelAttribute("customers")
	public Collection<Customer> customers() {
		return customerClient.findAll();
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView orderList() {
		return new ModelAndView("orderlist", "orders", paintingOrderRepository.findAll());
	}

	@RequestMapping(value = "/form.html", method = RequestMethod.GET)
	public ModelAndView form() {
		return new ModelAndView("orderForm", "order", new PaintingOrder());
	}

	@RequestMapping(value = "/line", method = RequestMethod.POST)
	public ModelAndView addLine(PaintingOrder paintingOrder) {
		paintingOrder.addLine(0, catalogClient.findAll().iterator().next().getItemId());
		return new ModelAndView("orderForm", "order", paintingOrder);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView get(@PathVariable("id") long id) {
		return new ModelAndView("order", "order", paintingOrderRepository.findById(id).get());
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ModelAndView post(PaintingOrder paintingOrder) {
		paintingOrder = paintingOrderService.order(paintingOrder);
		return new ModelAndView("success");
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ModelAndView post(@PathVariable("id") long id) {
		paintingOrderRepository.deleteById(id);

		return new ModelAndView("success");
	}

}
