package com.lms.repository;

import com.lms.model.Student;
import com.lms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUser(User user);
    Optional<Student> findByRegisterNumber(String registerNumber);
    List<Student> findByDepartment(String department);
}
