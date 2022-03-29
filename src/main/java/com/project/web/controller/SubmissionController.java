package com.project.web.controller;

import com.project.web.model.Submission;
import com.project.web.payload.response.ResponseObject;
import com.project.web.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path ="/submission")
@Slf4j
@CrossOrigin("http://localhost:3000")
public class SubmissionController {
    private final SubmissionService submissionServ;

    @GetMapping("/all")
    public List<Submission> getAllSubmission(@RequestParam int pageNumber) {
        return submissionServ.getAllSubmission(pageNumber);
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseObject> getSubmissionById(@PathVariable Long id) {
        return submissionServ.getSubmissionById(id);
    }

    @PreAuthorize("hasRole('QA_MANAGER') or hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addSubmission(@Valid @RequestBody Submission submission) {
        return submissionServ.addSubmission(submission);
    }

    @PreAuthorize("hasRole('QA_MANAGER') or hasRole('ADMIN')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<ResponseObject> editSubmission(@Valid @RequestBody Submission submission, @PathVariable Long id) {
        return submissionServ.editSubmission(submission, id);
    }

    @PreAuthorize("hasRole('QA_MANAGER') or hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteSubmission(@PathVariable Long id) {
        return submissionServ.deleteSubmission(id);
    }
}