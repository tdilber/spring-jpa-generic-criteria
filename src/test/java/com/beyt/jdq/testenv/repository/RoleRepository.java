package com.beyt.jdq.testenv.repository;

import com.beyt.jdq.repository.JpaDynamicQueryRepository;
import com.beyt.jdq.testenv.entity.authorization.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long>, JpaDynamicQueryRepository<Role, Long> {}
