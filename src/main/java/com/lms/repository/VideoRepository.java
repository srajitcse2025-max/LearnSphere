package com.lms.repository;

import com.lms.model.Video;
import com.lms.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByCourseOrderByOrderIndex(Course course);
}
