package com.project.web.repository;

import com.project.web.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    Boolean existsBySubmissionName(String submissionName);
}
