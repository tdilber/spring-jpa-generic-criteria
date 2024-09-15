package com.beyt.jdq.testenv.repository;

import com.beyt.jdq.repository.JpaDynamicQueryRepository;
import com.beyt.jdq.testenv.entity.authorization.Authorization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorizationRepository extends JpaRepository<Authorization, Long>, JpaDynamicQueryRepository<Authorization, Long> {}
