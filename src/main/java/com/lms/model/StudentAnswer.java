package com.lms.model;

import jakarta.persistence.*;

@Entity
@Table(name = "student_answers")
public class StudentAnswer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "quiz_attempt_id", nullable = false)
    private QuizAttempt quizAttempt;
    @ManyToOne @JoinColumn(name = "question_id", nullable = false)
    private Question question;
    private String selectedAnswer;
    private boolean correct;

    public StudentAnswer() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public QuizAttempt getQuizAttempt() { return quizAttempt; }
    public void setQuizAttempt(QuizAttempt quizAttempt) { this.quizAttempt = quizAttempt; }
    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }
    public String getSelectedAnswer() { return selectedAnswer; }
    public void setSelectedAnswer(String selectedAnswer) { this.selectedAnswer = selectedAnswer; }
    public boolean isCorrect() { return correct; }
    public void setCorrect(boolean correct) { this.correct = correct; }
}
