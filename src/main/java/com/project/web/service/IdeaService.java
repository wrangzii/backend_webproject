package com.project.web.service;

import com.project.web.model.Idea;
import com.project.web.payload.request.SubmitIdeaRequest;
import com.project.web.payload.response.ResponseObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IdeaService {
    List<Idea> getAllIdea(int pageNumber);
    ResponseEntity<ResponseObject> getIdeaById(Long id);
    ResponseEntity<ResponseObject> addIdea(SubmitIdeaRequest idea, MultipartFile file) throws Exception;
    ResponseEntity<ResponseObject> deleteIdea(Long id);
    ResponseEntity<ResponseObject> editIdea(SubmitIdeaRequest idea, Long id);
}
