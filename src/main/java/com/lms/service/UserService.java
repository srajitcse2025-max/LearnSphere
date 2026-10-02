package com.lms.service;

import com.lms.model.*;
import com.lms.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, StudentRepository studentRepository,
                       FacultyRepository facultyRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerStudent(String fullName, String email, String password,
                                 String registerNumber, String department, String section, String college) {
        User user = new User();
        user.setFullName(fullName); user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_STUDENT"); user.setActive(true);
        user = userRepository.save(user);

        Student student = new Student();
        student.setUser(user); student.setRegisterNumber(registerNumber);
        student.setDepartment(department); student.setSection(section);
        student.setCollege(college); student.setSemester(1);
        studentRepository.save(student);
        return user;
    }

    public User registerFaculty(String fullName, String email, String password,
                                 String employeeId, String department, String designation) {
        User user = new User();
        user.setFullName(fullName); user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_FACULTY"); user.setActive(true);
        user = userRepository.save(user);

        Faculty faculty = new Faculty();
        faculty.setUser(user); faculty.setEmployeeId(employeeId);
        faculty.setDepartment(department); faculty.setDesignation(designation);
        facultyRepository.save(faculty);
        return user;
    }

    public Optional<User> findByEmail(String email) { return userRepository.findByEmail(email); }
    public boolean emailExists(String email) { return userRepository.existsByEmail(email); }
    public List<User> findAll() { return userRepository.findAll(); }
}
