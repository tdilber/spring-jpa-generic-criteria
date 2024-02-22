package com.beyt.testenv.repository;

import com.beyt.repository.JpaDynamicQueryRepository;
import com.beyt.testenv.entity.Customer;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaDynamicQueryRepository<Customer, Long> {
}
