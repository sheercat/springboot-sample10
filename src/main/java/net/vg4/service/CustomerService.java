package net.vg4.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import net.vg4.domain.Customer;
import net.vg4.domain.User;
import net.vg4.repository.CustomerRepository;

@Service
@Slf4j
public class CustomerService {
    final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // public List<Customer> findAll() {
    // return customerRepository.findAllOrderByName();
    // }

    public List<Customer> findAll() {
        return customerRepository.findAllWithUserOrderByName();
    }

    public Customer findOne(Integer id) {
        return customerRepository.findOne(id);
    }

    @Transactional(readOnly = false)
    public Customer create(Customer customer, User user) {
        customer.setUser(user);
        return customerRepository.save(customer);
    }

    @Transactional(readOnly = false)
    public Customer update(Customer customer, User user) {
        customer.setUser(user);
        return customerRepository.save(customer);
    }

    @Transactional(readOnly = false)
    public void delete(Integer id) {
        try {
            if (customerRepository.exists(id)) {
                customerRepository.delete(id);
            } else {
                log.warn("not exists id", id);
            }
        } catch (EmptyResultDataAccessException e) {
            log.error("Error in delete:");
        }
    }

    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAllOrderByName(pageable);
    }

}
