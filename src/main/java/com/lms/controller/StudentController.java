package com.lms.controller;

import com.lms.model.*;
import com.lms.repository.*;
import com.lms.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
@RequestMapping("/student")
public class StudentController {
    private final UserRepository userRepo;
    private final StudentRepository studentRepo;
    private final CourseService courseService;
    private final QuizService quizService;
    private final AcademicService academicService;

    public StudentController(UserRepository userRepo, StudentRepository studentRepo,
                              CourseService courseService, QuizService quizService, AcademicService academicService) {
        this.userRepo = userRepo; this.studentRepo = studentRepo;
        this.courseService = courseService; this.quizService = quizService; this.academicService = academicService;
    }

    private Student getStudent(Authentication auth) {
        User user = userRepo.findByEmail(auth.getName()).orElseThrow();
        return studentRepo.findByUser(user).orElseThrow();
    }

    @GetMapping("/profile")
    public String profile(Authentication auth, Model model) {
        Student student = getStudent(auth);
        model.addAttribute("student", student);
        return "student/profile";
    }

    @GetMapping("/courses")
    public String courses(Authentication auth, Model model) {
        Student student = getStudent(auth);
        model.addAttribute("allCourses", courseService.findAll());
        model.addAttribute("enrollments", courseService.getEnrollments(student));
        model.addAttribute("student", student);
        return "student/courses";
    }

    @PostMapping("/courses/enroll/{courseId}")
    public String enroll(@PathVariable Long courseId, Authentication auth) {
        Student student = getStudent(auth);
        Course course = courseService.findById(courseId).orElseThrow();
        courseService.enroll(student, course);
        return "redirect:/student/courses";
    }

    @GetMapping("/courses/{courseId}/videos")
    public String courseVideos(@PathVariable Long courseId, Authentication auth, Model model) {
        Student student = getStudent(auth);
        Course course = courseService.findById(courseId).orElseThrow();
        model.addAttribute("course", course);
        model.addAttribute("videos", courseService.getVideos(course));
        return "student/videos";
    }

    @GetMapping("/quizzes")
    public String quizzes(Authentication auth, Model model) {
        Student student = getStudent(auth);
        List<Enrollment> enrollments = courseService.getEnrollments(student);
        List<Quiz> quizzes = new ArrayList<>();
        for (Enrollment e : enrollments) {
            quizzes.addAll(quizService.findByCourse(e.getCourse()));
        }
        model.addAttribute("quizzes", quizzes);
        model.addAttribute("attempts", quizService.getAttempts(student));
        return "student/quizzes";
    }

    @GetMapping("/quizzes/{quizId}/take")
    public String takeQuiz(@PathVariable Long quizId, Model model) {
        Quiz quiz = quizService.findById(quizId).orElseThrow();
        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", quizService.getQuestions(quiz));
        return "student/take-quiz";
    }

    @PostMapping("/quizzes/{quizId}/submit")
    public String submitQuiz(@PathVariable Long quizId, @RequestParam Map<String, String> params, Authentication auth) {
        Student student = getStudent(auth);
        Quiz quiz = quizService.findById(quizId).orElseThrow();
        Map<Long, String> answers = new HashMap<>();
        params.forEach((key, value) -> {
            if (key.startsWith("answer_")) {
                answers.put(Long.parseLong(key.replace("answer_", "")), value);
            }
        });
        QuizAttempt attempt = quizService.submitQuiz(student, quiz, answers);
        return "redirect:/student/quizzes/" + attempt.getId() + "/result";
    }

    @GetMapping("/quizzes/{attemptId}/result")
    public String quizResult(@PathVariable Long attemptId, Model model) {
        QuizAttempt attempt = quizService.getAttemptById(attemptId).orElseThrow();
        model.addAttribute("attempt", attempt);
        model.addAttribute("answers", quizService.getAnswers(attempt));
        return "student/quiz-result";
    }

    @GetMapping("/academics")
    public String academics(Authentication auth, Model model) {
        Student student = getStudent(auth);
        List<AcademicResult> results = academicService.getResults(student);
        model.addAttribute("student", student);
        model.addAttribute("results", results);
        model.addAttribute("gpa", academicService.calculateGPA(student, student.getSemester()));
        model.addAttribute("cgpa", academicService.calculateCGPA(student));
        return "student/academics";
    }

    @GetMapping("/performance")
    public String performance(Authentication auth, Model model) {
        Student student = getStudent(auth);
        model.addAttribute("student", student);
        model.addAttribute("attempts", quizService.getAttempts(student));
        model.addAttribute("results", academicService.getResults(student));
        model.addAttribute("cgpa", academicService.calculateCGPA(student));
        return "student/performance";
    }
}
