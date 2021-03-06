package com.groupproject.microservice.customer.web;

import com.groupproject.microservice.customer.Customer;
import com.groupproject.microservice.customer.CustomerRepository;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class CustomerController {

    private CustomerRepository customerRepository;

    @Autowired
    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @RequestMapping(value = "/{id}.html", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView customer(@PathVariable("id") long id) {
        return new ModelAndView("customer", "customer", customerRepository.findById(id).get());
    }

    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public ModelAndView customerList() {
        return new ModelAndView("customerlist", "customers", customerRepository.findAll());
    }

    @RequestMapping(value = "/form.html", method = RequestMethod.GET)
    public ModelAndView add() {
        return new ModelAndView("customer", "customer", new Customer());
    }

    @RequestMapping(value = "/form.html", method = RequestMethod.POST)
    public ModelAndView post(Customer customer, HttpServletRequest httpRequest) {
        customer = customerRepository.save(customer);
        return new ModelAndView("success");
    }

    @RequestMapping(value = "/{id}.html", method = RequestMethod.PUT)
    public ModelAndView put(@PathVariable("id") long id, Customer customer, HttpServletRequest httpRequest) {
        customer.setId(id);
        customerRepository.save(customer);
        return new ModelAndView("success");
    }

    @RequestMapping(value = "/{id}.html", method = RequestMethod.DELETE)
    public ModelAndView delete(@PathVariable("id") long id) {
        customerRepository.deleteById(id);
        return new ModelAndView("success");
    }

}

