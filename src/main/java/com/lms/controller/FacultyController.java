package com.lms.controller;

import com.lms.model.*;
import com.lms.repository.*;
import com.lms.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/faculty")
public class FacultyController {
    private final UserRepository userRepo;
    private final FacultyRepository facultyRepo;
    private final StudentRepository studentRepo;
    private final CourseService courseService;
    private final QuizService quizService;
    private final AcademicService academicService;
    private final SubjectRepository subjectRepo;
    private final AcademicResultRepository resultRepo;

    public FacultyController(UserRepository userRepo, FacultyRepository facultyRepo, StudentRepository studentRepo,
                              CourseService courseService, QuizService quizService, AcademicService academicService,
                              SubjectRepository subjectRepo, AcademicResultRepository resultRepo) {
        this.userRepo = userRepo; this.facultyRepo = facultyRepo; this.studentRepo = studentRepo;
        this.courseService = courseService; this.quizService = quizService; this.academicService = academicService;
        this.subjectRepo = subjectRepo; this.resultRepo = resultRepo;
    }

    private Faculty getFaculty(Authentication auth) {
        User user = userRepo.findByEmail(auth.getName()).orElseThrow();
        return facultyRepo.findByUser(user).orElseThrow();
    }

    @GetMapping("/profile")
    public String profile(Authentication auth, Model model) {
        model.addAttribute("faculty", getFaculty(auth));
        return "faculty/profile";
    }

    @GetMapping("/courses")
    public String courses(Authentication auth, Model model) {
        Faculty faculty = getFaculty(auth);
        model.addAttribute("courses", courseService.findByFaculty(faculty));
        return "faculty/courses";
    }

    @GetMapping("/courses/new")
    public String newCourse() { return "faculty/course-form"; }

    @PostMapping("/courses/save")
    public String saveCourse(@RequestParam String title, @RequestParam String description,
                              @RequestParam String courseCode, @RequestParam String department, Authentication auth) {
        Faculty faculty = getFaculty(auth);
        Course course = new Course(); course.setTitle(title); course.setDescription(description);
        course.setCourseCode(courseCode); course.setDepartment(department); course.setFaculty(faculty);
        courseService.save(course);
        return "redirect:/faculty/courses";
    }

    @GetMapping("/courses/{courseId}/videos")
    public String manageVideos(@PathVariable Long courseId, Model model) {
        Course course = courseService.findById(courseId).orElseThrow();
        model.addAttribute("course", course);
        model.addAttribute("videos", courseService.getVideos(course));
        return "faculty/manage-videos";
    }

    @PostMapping("/courses/{courseId}/videos/add")
    public String addVideo(@PathVariable Long courseId, @RequestParam String title,
                            @RequestParam String description, @RequestParam String youtubeUrl, @RequestParam int orderIndex) {
        Course course = courseService.findById(courseId).orElseThrow();
        courseService.addVideo(course, title, description, youtubeUrl, orderIndex);
        return "redirect:/faculty/courses/" + courseId + "/videos";
    }

    @GetMapping("/courses/{courseId}/students")
    public String courseStudents(@PathVariable Long courseId, Model model) {
        Course course = courseService.findById(courseId).orElseThrow();
        model.addAttribute("course", course);
        model.addAttribute("enrollments", courseService.getCourseEnrollments(course));
        return "faculty/course-students";
    }

    @GetMapping("/quizzes/{courseId}")
    public String quizzes(@PathVariable Long courseId, Model model) {
        Course course = courseService.findById(courseId).orElseThrow();
        model.addAttribute("course", course);
        model.addAttribute("quizzes", quizService.findByCourse(course));
        return "faculty/quizzes";
    }

    @PostMapping("/quizzes/{courseId}/create")
    public String createQuiz(@PathVariable Long courseId, @RequestParam String title,
                              @RequestParam String topic, @RequestParam int durationMinutes) {
        Course course = courseService.findById(courseId).orElseThrow();
        Quiz quiz = new Quiz(); quiz.setCourse(course); quiz.setTitle(title);
        quiz.setTopic(topic); quiz.setDurationMinutes(durationMinutes); quiz.setTotalMarks(0);
        quizService.save(quiz);
        return "redirect:/faculty/quizzes/" + courseId;
    }

    @GetMapping("/quizzes/{quizId}/questions")
    public String manageQuestions(@PathVariable Long quizId, Model model) {
        Quiz quiz = quizService.findById(quizId).orElseThrow();
        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", quizService.getQuestions(quiz));
        return "faculty/manage-questions";
    }

    @PostMapping("/quizzes/{quizId}/questions/add")
    public String addQuestion(@PathVariable Long quizId, @RequestParam String questionText,
                               @RequestParam String optionA, @RequestParam String optionB,
                               @RequestParam String optionC, @RequestParam String optionD,
                               @RequestParam String correctAnswer, @RequestParam String explanation) {
        Quiz quiz = quizService.findById(quizId).orElseThrow();
        quizService.addQuestion(quiz, questionText, optionA, optionB, optionC, optionD, correctAnswer, explanation);
        return "redirect:/faculty/quizzes/" + quizId + "/questions";
    }

    @GetMapping("/marks")
    public String enterMarks(Authentication auth, Model model) {
        model.addAttribute("students", studentRepo.findAll());
        model.addAttribute("subjects", subjectRepo.findAll());
        return "faculty/enter-marks";
    }

    @PostMapping("/marks/save")
    public String saveMarks(@RequestParam Long studentId, @RequestParam Long subjectId,
                             @RequestParam int internalMarks, @RequestParam int externalMarks, @RequestParam int semester) {
        Student student = studentRepo.findById(studentId).orElseThrow();
        Subject subject = subjectRepo.findById(subjectId).orElseThrow();
        AcademicResult result = new AcademicResult();
        result.setStudent(student); result.setSubject(subject);
        result.setInternalMarks(internalMarks); result.setExternalMarks(externalMarks); result.setSemester(semester);
        academicService.saveResult(result);
        return "redirect:/faculty/marks?success";
    }
}
