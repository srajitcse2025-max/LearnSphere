package com.lms.service;

import com.lms.model.*;
import com.lms.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final StudentAnswerRepository studentAnswerRepository;

    public QuizService(QuizRepository quizRepository, QuestionRepository questionRepository,
                       QuizAttemptRepository quizAttemptRepository, StudentAnswerRepository studentAnswerRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.studentAnswerRepository = studentAnswerRepository;
    }

    public List<Quiz> findByCourse(Course course) { return quizRepository.findByCourse(course); }
    public Optional<Quiz> findById(Long id) { return quizRepository.findById(id); }
    public Quiz save(Quiz quiz) { return quizRepository.save(quiz); }

    public Question addQuestion(Quiz quiz, String text, String a, String b, String c, String d, String correct, String explanation) {
        Question q = new Question();
        q.setQuiz(quiz); q.setQuestionText(text);
        q.setOptionA(a); q.setOptionB(b); q.setOptionC(c); q.setOptionD(d);
        q.setCorrectAnswer(correct); q.setExplanation(explanation);
        return questionRepository.save(q);
    }

    public List<Question> getQuestions(Quiz quiz) { return questionRepository.findByQuiz(quiz); }

    public QuizAttempt submitQuiz(Student student, Quiz quiz, Map<Long, String> answers) {
        List<Question> questions = questionRepository.findByQuiz(quiz);
        int score = 0;

        QuizAttempt attempt = new QuizAttempt();
        attempt.setStudent(student); attempt.setQuiz(quiz);
        attempt.setTotalQuestions(questions.size());
        attempt.setAttemptDate(LocalDateTime.now());
        attempt = quizAttemptRepository.save(attempt);

        List<StudentAnswer> studentAnswers = new ArrayList<>();
        for (Question q : questions) {
            String selected = answers.getOrDefault(q.getId(), "");
            boolean isCorrect = q.getCorrectAnswer().equalsIgnoreCase(selected);
            if (isCorrect) score++;
            StudentAnswer sa = new StudentAnswer();
            sa.setQuizAttempt(attempt); sa.setQuestion(q);
            sa.setSelectedAnswer(selected); sa.setCorrect(isCorrect);
            studentAnswers.add(sa);
        }
        studentAnswerRepository.saveAll(studentAnswers);
        attempt.setScore(score);
        attempt.setPercentage(questions.isEmpty() ? 0 : (score * 100.0) / questions.size());
        return quizAttemptRepository.save(attempt);
    }

    public List<QuizAttempt> getAttempts(Student student) { return quizAttemptRepository.findByStudent(student); }
    public List<QuizAttempt> getAttemptsByQuiz(Quiz quiz) { return quizAttemptRepository.findByQuiz(quiz); }
    public Optional<QuizAttempt> getAttemptById(Long id) { return quizAttemptRepository.findById(id); }
    public List<StudentAnswer> getAnswers(QuizAttempt attempt) { return studentAnswerRepository.findByQuizAttempt(attempt); }
}
