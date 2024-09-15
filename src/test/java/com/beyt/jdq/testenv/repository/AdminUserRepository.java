package com.beyt.jdq.testenv.repository;

import com.beyt.jdq.repository.JpaDynamicQueryRepository;
import com.beyt.jdq.testenv.entity.authorization.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long>, JpaDynamicQueryRepository<AdminUser, Long> {}
