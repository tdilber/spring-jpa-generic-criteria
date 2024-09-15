package com.beyt.jdq.presetation;

import com.beyt.jdq.BaseTestInstance;
import com.beyt.jdq.TestApplication;
import com.beyt.jdq.dto.Criteria;
import com.beyt.jdq.dto.CriteriaList;
import com.beyt.jdq.dto.enums.CriteriaOperator;
import com.beyt.jdq.testenv.entity.school.Department;
import com.beyt.jdq.testenv.entity.school.Student;
import com.beyt.jdq.testenv.repository.DepartmentRepository;
import com.beyt.jdq.testenv.repository.StudentRepository;
import com.beyt.jdq.util.PresentationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = TestApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class S5_Join extends BaseTestInstance {
    private @Autowired DepartmentRepository departmentRepository;
    private @Autowired StudentRepository studentRepository;


    /**
     * Inner join with single entity criteria
     */
    @Test
    public void  innerJoin() {
        var criteriaList = CriteriaList.of(Criteria.of("department.name", CriteriaOperator.START_WITH, "P"));
        PresentationUtil.prettyPrint(criteriaList);
        List<Student> students = studentRepository.findAll(criteriaList);
        PresentationUtil.prettyPrint(students);
        assertEquals(List.of(student3, student9), students);
    }

    /**
     * Inner join with multi joined entity criteria
     */
    @Test
    public void innerJoin2() {
        var criteriaList = CriteriaList.of(
                Criteria.of("department.name", CriteriaOperator.START_WITH, "P"),
                Criteria.of("department.id", CriteriaOperator.GREATER_THAN, 3)
        );
        PresentationUtil.prettyPrint(criteriaList);
        List<Student> students = studentRepository.findAll(criteriaList);
        PresentationUtil.prettyPrint(students);
        assertEquals(List.of( student9), students);
    }

    /**
     * Inner join with different entity criteria
     */
    @Test
    public void innerJoin3() {
        var criteriaList = CriteriaList.of(
                Criteria.of("department.name", CriteriaOperator.START_WITH, "P"),
                Criteria.of("name", CriteriaOperator.START_WITH, "Robert")
        );
        PresentationUtil.prettyPrint(criteriaList);
        List<Student> students = studentRepository.findAll(criteriaList);
        PresentationUtil.prettyPrint(students);
        assertEquals(List.of(student3), students);
    }

    /**
     * One to many inner join
     */
    @Test
    public void innerJoin4() {
        var criteriaList = CriteriaList.of(
                Criteria.of("students.id", CriteriaOperator.GREATER_THAN, 3),
                Criteria.of("id", CriteriaOperator.LESS_THAN, 6)
        );
        PresentationUtil.prettyPrint(criteriaList);
        List<Department> departments = departmentRepository.findAll(criteriaList);
        PresentationUtil.prettyPrint(departments);
        assertEquals(List.of(department4, department5), departments);
    }

    /**
     * Many to many inner join
     */
    @Test
    public void innerJoin5() {
        var criteriaList = CriteriaList.of(
                Criteria.of("courses.maxStudentCount", CriteriaOperator.GREATER_THAN, 100),
                Criteria.of("id", CriteriaOperator.GREATER_THAN, 3)
        );
        PresentationUtil.prettyPrint(criteriaList);
        List<Student> students = studentRepository.findAll(criteriaList);
        PresentationUtil.prettyPrint(students);
        assertEquals(List.of(student4, student5), students);
    }

    /**
     * Typical left join with is null check
     */
    @Test
    public void leftJoin() {
        var criteriaList = CriteriaList.of(
                Criteria.of("department<id", CriteriaOperator.SPECIFIED, false),
                Criteria.of("id", CriteriaOperator.GREATER_THAN, 3)
        );
        PresentationUtil.prettyPrint(criteriaList);
        List<Student> students = studentRepository.findAll(criteriaList);
        PresentationUtil.prettyPrint(students);
        assertEquals(List.of(student11), students);
    }
}
