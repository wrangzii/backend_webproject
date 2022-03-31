package com.project.web.controller;

import com.project.web.payload.request.CommentRequest;
import com.project.web.payload.response.CommentResponse;
import com.project.web.payload.response.ResponseObject;
import com.project.web.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/comment")
@Slf4j
@CrossOrigin("http://localhost:3000")
public class CommentController {
    private final CommentService commentSer;

    @GetMapping("/all/{ideaId}")
    public List<CommentResponse> getAllCommentByIdea(@PathVariable Long ideaId) {
        return commentSer.getAllCommentByIdea(ideaId);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addCommentOfUser(@Valid @RequestBody CommentRequest comment,
                                                           @RequestParam(required = false) Long parentCommentId) {
        return commentSer.addCommentOfUser(comment, parentCommentId);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ResponseObject> editComment(@Valid @RequestBody CommentRequest comment, @PathVariable Long id,
                                                      @RequestParam(required = false) Long parentCommentId) {
        return commentSer.editComment(comment, id, parentCommentId);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteComment(@PathVariable Long id) {
        return commentSer.deleteComment(id);
    }
}
