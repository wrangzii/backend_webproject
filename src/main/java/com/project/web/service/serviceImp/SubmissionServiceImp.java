package com.project.web.service.serviceImp;

import com.project.web.model.Submission;
import com.project.web.payload.response.ResponseObject;
import com.project.web.repository.SubmissionRepository;
import com.project.web.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SubmissionServiceImp implements SubmissionService {
    private final SubmissionRepository submissionRepo;

    @Override
    public List<Submission> getAllSubmission() {
        return submissionRepo.findAll();
    }

    @Override
    public ResponseEntity<ResponseObject> addSubmission(Submission submission) {
        Boolean checkExisted = submissionRepo.existsBySubmissionName(submission.getSubmissionName());
        if (!checkExisted) {
            submissionRepo.save(submission);
            return ResponseEntity.ok(new ResponseObject(HttpStatus.CREATED.toString(),"Add submission successfully!", submission));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(),"Error: Submission name is already taken!"));
    }

    @Override
    public ResponseEntity<ResponseObject> deleteSubmission(Long id) {
        Optional<Submission> deleteSubmission = submissionRepo.findById(id);
        if (deleteSubmission.isPresent()) {
            submissionRepo.deleteById(id);
            return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(),"Delete submission successfully!"));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(),"Error: Submission name is not exist!"));
    }

    @Override
    public ResponseEntity<ResponseObject> editSubmission(Submission submission, Long id) {
        Optional<Submission> editSubmission = submissionRepo.findById(id);
        if (editSubmission.isPresent()) {
            editSubmission.get().setSubmissionName(submission.getSubmissionName());
            editSubmission.get().setDescription(submission.getDescription());
            editSubmission.get().setClosureDate(submission.getClosureDate());
            editSubmission.get().setFinalClosureDate(submission.getFinalClosureDate());
            submissionRepo.save(editSubmission.get());
            return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(),"Edit submission successfully!",editSubmission));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(),"Error: Submission is not exist!"));
    }
}