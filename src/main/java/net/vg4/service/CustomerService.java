package net.vg4.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.vg4.domain.Customer;
import net.vg4.domain.User;
import net.vg4.repository.CustomerRepository;

@Service
@Transactional
public class CustomerService {
	@Autowired
	CustomerRepository customerRepository;

	// public List<Customer> findAll() {
	// return customerRepository.findAllOrderByName();
	// }

	public List<Customer> findAll() {
		return customerRepository.findAllWithUserOrderByName();
	}

	public Customer findOne(Integer id) {
		return customerRepository.findOne(id);
	}

	public Customer create(Customer customer, User user) {
		customer.setUser(user);
		return customerRepository.save(customer);
	}

	public Customer update(Customer customer, User user) {
		customer.setUser(user);
		return customerRepository.save(customer);
	}

	public void delete(Integer id) {
		customerRepository.delete(id);
	}

	public Page<Customer> findAll(Pageable pageable) {
		return customerRepository.findAllOrderByName(pageable);
	}

}
