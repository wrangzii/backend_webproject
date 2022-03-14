package com.project.web.controller;

import com.project.web.model.Idea;
import com.project.web.payload.request.SubmitIdeaRequest;
import com.project.web.payload.response.ResponseObject;
import com.project.web.service.IdeaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public List<Idea> getAllIdea(@RequestParam int pageNumber) {return ideaService.getAllIdea(pageNumber);}

    @GetMapping("{id}")
    public ResponseEntity<ResponseObject> getIdeaById(@PathVariable Long id) {
        return ideaService.getIdeaById(id);
    }

    @PostMapping(value = "/add",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<ResponseObject> addIdea(@Valid @ModelAttribute SubmitIdeaRequest idea, @RequestBody MultipartFile file) throws Exception {
        return ideaService.addIdea(idea, file);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ResponseObject> editIdea(@Valid @ModelAttribute SubmitIdeaRequest idea, @PathVariable Long id) {
        return ideaService.editIdea(idea, id);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteIdea(@PathVariable Long id) {
        return ideaService.deleteIdea(id);
    }
}
