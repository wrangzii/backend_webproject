package com.project.web.service;

import com.project.web.model.Submission;
import com.project.web.payload.response.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SubmissionService {
    List<Submission> getAllSubmission(int pageNumber);
    ResponseEntity<ResponseObject> getSubmissionById(Long id);
    ResponseEntity<ResponseObject> addSubmission(Submission submission);
    ResponseEntity<ResponseObject> deleteSubmission(Long id);
    ResponseEntity<ResponseObject> editSubmission(Submission submission, Long id);
}
