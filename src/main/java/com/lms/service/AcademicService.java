package com.lms.service;

import com.lms.model.*;
import com.lms.repository.*;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AcademicService {
    private final AcademicResultRepository resultRepository;
    private final SubjectRepository subjectRepository;

    public AcademicService(AcademicResultRepository resultRepository, SubjectRepository subjectRepository) {
        this.resultRepository = resultRepository;
        this.subjectRepository = subjectRepository;
    }

    public List<AcademicResult> getResults(Student student) { return resultRepository.findByStudent(student); }
    public List<AcademicResult> getResultsBySemester(Student student, int semester) {
        return resultRepository.findByStudentAndSemester(student, semester);
    }

    public AcademicResult saveResult(AcademicResult result) {
        result.setTotalMarks(result.getInternalMarks() + result.getExternalMarks());
        result.setGrade(calculateGrade(result.getTotalMarks()));
        result.setGradePoint(calculateGradePoint(result.getTotalMarks()));
        return resultRepository.save(result);
    }

    public double calculateGPA(Student student, int semester) {
        List<AcademicResult> results = resultRepository.findByStudentAndSemester(student, semester);
        if (results.isEmpty()) return 0.0;
        double totalWeighted = 0, totalCredits = 0;
        for (AcademicResult r : results) {
            int credits = r.getSubject().getCredits();
            totalWeighted += r.getGradePoint() * credits;
            totalCredits += credits;
        }
        return totalCredits > 0 ? Math.round((totalWeighted / totalCredits) * 100.0) / 100.0 : 0.0;
    }

    public double calculateCGPA(Student student) {
        List<AcademicResult> all = resultRepository.findByStudent(student);
        if (all.isEmpty()) return 0.0;
        Set<Integer> semesters = all.stream().map(AcademicResult::getSemester).collect(Collectors.toSet());
        double totalGPA = 0;
        for (int sem : semesters) totalGPA += calculateGPA(student, sem);
        return Math.round((totalGPA / semesters.size()) * 100.0) / 100.0;
    }

    public String calculateGrade(int totalMarks) {
        if (totalMarks >= 90) return "O";
        if (totalMarks >= 80) return "A+";
        if (totalMarks >= 70) return "A";
        if (totalMarks >= 60) return "B+";
        if (totalMarks >= 50) return "B";
        if (totalMarks >= 40) return "C";
        return "F";
    }

    public double calculateGradePoint(int totalMarks) {
        if (totalMarks >= 90) return 10.0;
        if (totalMarks >= 80) return 9.0;
        if (totalMarks >= 70) return 8.0;
        if (totalMarks >= 60) return 7.0;
        if (totalMarks >= 50) return 6.0;
        if (totalMarks >= 40) return 5.0;
        return 0.0;
    }

    public List<Subject> getSubjects(String department) { return subjectRepository.findByDepartment(department); }
}
