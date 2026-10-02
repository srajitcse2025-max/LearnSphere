package com.lms.model;

import jakarta.persistence.*;

@Entity
@Table(name = "academic_results")
public class AcademicResult {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    @ManyToOne @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;
    private int internalMarks;
    private int externalMarks;
    private int totalMarks;
    private String grade;
    private double gradePoint;
    private int semester;

    public AcademicResult() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }
    public int getInternalMarks() { return internalMarks; }
    public void setInternalMarks(int internalMarks) { this.internalMarks = internalMarks; }
    public int getExternalMarks() { return externalMarks; }
    public void setExternalMarks(int externalMarks) { this.externalMarks = externalMarks; }
    public int getTotalMarks() { return totalMarks; }
    public void setTotalMarks(int totalMarks) { this.totalMarks = totalMarks; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public double getGradePoint() { return gradePoint; }
    public void setGradePoint(double gradePoint) { this.gradePoint = gradePoint; }
    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }
}
