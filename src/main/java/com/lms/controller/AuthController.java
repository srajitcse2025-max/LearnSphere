package com.lms.controller;

import com.lms.model.*;
import com.lms.repository.*;
import com.lms.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public AuthController(UserService userService, UserRepository userRepository,
                          StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    @GetMapping("/")
    public String home() { return "redirect:/dashboard"; }

    @GetMapping("/login")
    public String login() { return "auth/login"; }

    @GetMapping("/register")
    public String register() { return "auth/register"; }

    @PostMapping("/register")
    public String doRegister(@RequestParam String fullName, @RequestParam String email,
                              @RequestParam String password, @RequestParam String role,
                              @RequestParam(required = false) String registerNumber,
                              @RequestParam(required = false) String department,
                              @RequestParam(required = false) String section,
                              @RequestParam(required = false) String college,
                              @RequestParam(required = false) String employeeId,
                              @RequestParam(required = false) String designation, Model model) {
        if (userService.emailExists(email)) {
            model.addAttribute("error", "Email already exists");
            return "auth/register";
        }
        if ("ROLE_STUDENT".equals(role)) {
            userService.registerStudent(fullName, email, password, registerNumber, department, section, college);
        } else {
            userService.registerFaculty(fullName, email, password, employeeId, department, designation);
        }
        return "redirect:/login?success";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        model.addAttribute("user", user);
        if (user.getRole().equals("ROLE_STUDENT")) {
            Student student = studentRepository.findByUser(user).orElseThrow();
            model.addAttribute("student", student);
            return "student/dashboard";
        } else if (user.getRole().equals("ROLE_FACULTY")) {
            Faculty faculty = facultyRepository.findByUser(user).orElseThrow();
            model.addAttribute("faculty", faculty);
            return "faculty/dashboard";
        }
        return "admin/dashboard";
    }
}
