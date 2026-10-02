package com.lms.service;

import com.lms.model.*;
import com.lms.repository.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final VideoRepository videoRepository;
    private final EnrollmentRepository enrollmentRepository;

    public CourseService(CourseRepository courseRepository, VideoRepository videoRepository,
                         EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.videoRepository = videoRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<Course> findAll() { return courseRepository.findAll(); }
    public Optional<Course> findById(Long id) { return courseRepository.findById(id); }
    public List<Course> findByFaculty(Faculty faculty) { return courseRepository.findByFaculty(faculty); }
    public Course save(Course course) { return courseRepository.save(course); }
    public void delete(Long id) { courseRepository.deleteById(id); }

    public Video addVideo(Course course, String title, String description, String youtubeUrl, int order) {
        Video video = new Video();
        video.setCourse(course); video.setTitle(title); video.setDescription(description);
        video.setYoutubeUrl(youtubeUrl); video.setOrderIndex(order);
        return videoRepository.save(video);
    }

    public List<Video> getVideos(Course course) { return videoRepository.findByCourseOrderByOrderIndex(course); }

    public Enrollment enroll(Student student, Course course) {
        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            return enrollmentRepository.findByStudentAndCourse(student, course).get();
        }
        Enrollment e = new Enrollment();
        e.setStudent(student); e.setCourse(course); e.setProgress(0); e.setCompleted(false);
        return enrollmentRepository.save(e);
    }

    public List<Enrollment> getEnrollments(Student student) { return enrollmentRepository.findByStudent(student); }
    public List<Enrollment> getCourseEnrollments(Course course) { return enrollmentRepository.findByCourse(course); }
    public boolean isEnrolled(Student student, Course course) { return enrollmentRepository.existsByStudentAndCourse(student, course); }
}
