package com.lms.repository;

import com.lms.model.StudentAnswer;
import com.lms.model.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {
    List<StudentAnswer> findByQuizAttempt(QuizAttempt quizAttempt);
}
