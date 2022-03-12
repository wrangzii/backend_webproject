package com.project.web.service;

import com.project.web.model.Idea;
import com.project.web.payload.response.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IdeaService {
    List<Idea> getAllIdea();
    ResponseEntity<ResponseObject> addIdea(Idea idea);
    ResponseEntity<ResponseObject> deleteIdea(Long id);
    ResponseEntity<ResponseObject> editIdea(Idea idea, Long id);
}
