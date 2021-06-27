package com.beyt.testenv.repository;

import com.beyt.repository.GenericSpecificationRepository;
import com.beyt.testenv.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends GenericSpecificationRepository<User, Long> {
}

