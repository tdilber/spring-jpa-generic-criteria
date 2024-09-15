package com.beyt.jdq.testenv.repository;

import com.beyt.jdq.repository.JpaDynamicQueryRepository;
import com.beyt.jdq.testenv.entity.school.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaDynamicQueryRepository<Student, Long> {
}
