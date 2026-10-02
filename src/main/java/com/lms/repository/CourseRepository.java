package com.lms.repository;

import com.lms.model.Course;
import com.lms.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByFaculty(Faculty faculty);
    List<Course> findByDepartment(String department);
}
