package com.beyt.jdq.testenv.repository;

import com.beyt.jdq.repository.JpaDynamicQueryRepository;
import com.beyt.jdq.testenv.entity.school.Course;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaDynamicQueryRepository<Course, Long> {
}
