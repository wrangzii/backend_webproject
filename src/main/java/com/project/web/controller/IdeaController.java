package com.project.web.controller;

import com.project.web.model.Idea;
import com.project.web.payload.response.ResponseObject;
import com.project.web.service.IdeaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path ="/submit_idea")
@Slf4j
@CrossOrigin("http://localhost:3000")
public class IdeaController {
    private final IdeaService ideaService;

    @GetMapping("/all")
    public List<Idea> getAllIdea() {
        return ideaService.getAllIdea();
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addIdea(@Valid @RequestBody Idea idea) {
        return ideaService.addIdea(idea);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ResponseObject> editIdea(@Valid @RequestBody Idea idea, @PathVariable Long id) {
        return ideaService.editIdea(idea, id);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteIdea(@PathVariable Long id) {
        return ideaService.deleteIdea(id);
    }
}
