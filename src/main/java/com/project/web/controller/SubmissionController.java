package com.project.web.controller;

import com.project.web.model.Submission;
import com.project.web.payload.response.ResponseObject;
import com.project.web.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    public List<Submission> getAllSubmission() {
        return submissionServ.getAllSubmission();
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addSubmission(@Valid @RequestBody Submission submission) {
        return submissionServ.addSubmission(submission);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ResponseObject> editSubmission(@Valid @RequestBody Submission submission, @PathVariable Long id) {
        return submissionServ.editSubmission(submission, id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteSubmission(@PathVariable Long id) {
        return submissionServ.deleteSubmission(id);
    }
}