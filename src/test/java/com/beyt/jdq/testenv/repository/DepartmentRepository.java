package com.beyt.jdq.testenv.repository;

import com.beyt.jdq.repository.JpaDynamicQueryRepository;
import com.beyt.jdq.testenv.entity.school.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaDynamicQueryRepository<Department, Long> {
}
