package com.lms.repository;

import com.lms.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findBySemesterAndDepartment(int semester, String department);
    List<Subject> findByDepartment(String department);
}
