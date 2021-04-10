package com.beyt.testenv.repository;

import com.beyt.repository.JpaExtendedRepository;
import com.beyt.testenv.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaExtendedRepository<User, Long> {
}

