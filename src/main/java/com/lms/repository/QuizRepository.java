package com.lms.repository;

import com.lms.model.Quiz;
import com.lms.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByCourse(Course course);
}
