package com.beyt.testenv.controller;

import com.beyt.dto.CriteriaFilter;
import com.beyt.dto.SearchQuery;
import com.beyt.testenv.entity.Customer;
import com.beyt.testenv.entity.User;
import com.beyt.testenv.repository.CustomerRepository;
import com.beyt.testenv.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test-api")
public class TestController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;


    @GetMapping("/user")
    public ResponseEntity<List<User>> getUserWithCriteria(CriteriaFilter criteriaFilter) {
        List<User> userList = userRepository.findAllWithCriteria(criteriaFilter);
        return ResponseEntity.ok().body(userList);
    }

    @GetMapping("/customer")
    public ResponseEntity<List<Customer>> getCustomerWithCriteria(CriteriaFilter criteriaFilter) {
        List<Customer> customerList = customerRepository.findAllWithCriteria(criteriaFilter);
        return ResponseEntity.ok().body(customerList);
    }

    @GetMapping("/user/as-page")
    public ResponseEntity<Page<User>> getUserWithSearchFilterAsPage(SearchQuery searchQuery) {
        Page<User> userList = userRepository.findAllWithSearchQueryAsPage(searchQuery);
        return ResponseEntity.ok().body(userList);
    }

    @GetMapping("/user/as-list")
    public ResponseEntity<List<User>> getUserWithSearchFilterAsList(SearchQuery searchQuery) {
        List<User> userList = userRepository.findAllWithSearchQuery(searchQuery);
        return ResponseEntity.ok().body(userList);
    }

    @GetMapping("/customer/as-page")
    public ResponseEntity<Page<Customer>> getCustomerWithSearchFilterAsPage(SearchQuery searchQuery) {
        Page<Customer> customerList = customerRepository.findAllWithSearchQueryAsPage(searchQuery);
        return ResponseEntity.ok().body(customerList);
    }

    @GetMapping("/customer/as-list")
    public ResponseEntity<List<Customer>> getCustomerWithSearchFilterAsList(SearchQuery searchQuery) {
        List<Customer> customerList = customerRepository.findAllWithSearchQuery(searchQuery);
        return ResponseEntity.ok().body(customerList);
    }
}
