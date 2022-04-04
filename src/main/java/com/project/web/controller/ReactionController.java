package com.project.web.controller;

import com.project.web.payload.request.DeleteReactionRequest;
import com.project.web.payload.request.ReactionRequest;
import com.project.web.payload.response.ResponseObject;
import com.project.web.service.ReactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/reaction")
@Slf4j
@CrossOrigin("http://localhost:3000")
public class ReactionController {
    private final ReactionService reactionService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getReactionOfAnIdea(@PathVariable Long id) {
        return reactionService.getReactionOfAnIdea(id);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addReactionOfAnIdea(@Valid @RequestBody ReactionRequest reaction) {
        return reactionService.addReactionOfAnIdea(reaction);
    }

    @PutMapping("/edit")
    public ResponseEntity<ResponseObject> editReactionOfAnIdea(@Valid @RequestBody ReactionRequest reaction) {
        return reactionService.editReactionOfAnIdea(reaction);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseObject> deleteReactionOfAnIdea(@Valid @RequestBody DeleteReactionRequest deleteRequest) {
        return reactionService.deleteReactionOfAnIdea(deleteRequest);
    }
}
