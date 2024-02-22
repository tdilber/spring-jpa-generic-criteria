package com.beyt.testenv.repository;

import com.beyt.repository.DynamicSpecificationRepository;
import com.beyt.testenv.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends DynamicSpecificationRepository<User, Long> {
}

