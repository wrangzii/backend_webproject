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
    public void addReactionOfAnIdea(@Valid @RequestBody ReactionRequest reaction) {
        reactionService.addReactionOfAnIdea(reaction);
    }

    @PutMapping("/edit")
    public void editReactionOfAnIdea(@Valid @RequestBody ReactionRequest reaction) {
        reactionService.editReactionOfAnIdea(reaction);
    }

    @DeleteMapping("/delete")
    public void deleteReactionOfAnIdea(@Valid @RequestBody DeleteReactionRequest deleteRequest) {
        reactionService.deleteReactionOfAnIdea(deleteRequest);
    }
}
