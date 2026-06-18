package com.josan.study_buddy.Topic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    boolean existsByTitleAndSubjectId(String title, Long subjectId);
    boolean existsByTitleAndSubjectIdAndIdNot(String title, Long subjectId, Long id);
    List<Topic> findByUserIdAndNextReviewAtLessThanEqual(Long userId, LocalDate date);
}
