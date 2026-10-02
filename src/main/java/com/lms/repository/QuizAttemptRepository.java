package com.lms.repository;

import com.lms.model.QuizAttempt;
import com.lms.model.Student;
import com.lms.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    List<QuizAttempt> findByStudent(Student student);
    List<QuizAttempt> findByStudentAndQuiz(Student student, Quiz quiz);
    List<QuizAttempt> findByQuiz(Quiz quiz);
}
