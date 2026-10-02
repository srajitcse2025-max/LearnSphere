package com.lms.repository;

import com.lms.model.AcademicResult;
import com.lms.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AcademicResultRepository extends JpaRepository<AcademicResult, Long> {
    List<AcademicResult> findByStudent(Student student);
    List<AcademicResult> findByStudentAndSemester(Student student, int semester);
}
