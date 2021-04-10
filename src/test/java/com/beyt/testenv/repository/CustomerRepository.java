package com.beyt.testenv.repository;

import com.beyt.repository.JpaExtendedRepository;
import com.beyt.testenv.entity.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaExtendedRepository<Customer, Long> {
}
