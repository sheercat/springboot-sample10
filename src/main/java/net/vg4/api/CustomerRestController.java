package net.vg4.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.val;

import net.vg4.domain.Customer;
import net.vg4.service.CustomerService;

@RestController
@RequestMapping("api/customers")
@val
public class CustomerRestController {
    final CustomerService customerService;

    @Autowired
    public CustomerRestController(CustomerService customerService) {this.customerService = customerService;}

    @RequestMapping(method = RequestMethod.GET)
    Page<Customer> getCustomer(@PageableDefault Pageable pageable) {
        val customers = customerService.findAll(pageable);
        return customers;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    Customer getCustomer(@PathVariable Integer id) {
        val customer = customerService.findOne(id);
        return customer;
    }

//	@RequestMapping(method = RequestMethod.POST)
//	@ResponseStatus(HttpStatus.CREATED)
//	Customer postCustomers(@RequestBody Customer customer) {
//		return customerService.create(customer);
//	}

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<Customer> postCustomers(@Validated @RequestBody Customer customer,
                                           UriComponentsBuilder uriBuilder) {
        val created = customerService.create(customer, customer.getUser());
        val loc = uriBuilder.path("api/customers/{id}").buildAndExpand(created.getId()).toUri();
        val headers = new HttpHeaders();
        headers.setLocation(loc);
        return new ResponseEntity<>(created, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    Customer postCustomer(@PathVariable Integer id, @Validated @RequestBody Customer customer) {
        customer.setId(id);
        return customerService.update(customer, customer.getUser());
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCustomers(@Validated @PathVariable Integer id) {
        customerService.delete(id);
    }

}
