package com.project.web.service;

import com.project.web.payload.request.DeleteReactionRequest;
import com.project.web.payload.request.ReactionRequest;
import com.project.web.payload.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface ReactionService {
    ResponseEntity<ResponseObject> getReactionOfAnIdea(Long ideaId);
    ResponseEntity<ResponseObject> addReactionOfAnIdea(ReactionRequest reaction);
    ResponseEntity<ResponseObject> editReactionOfAnIdea(ReactionRequest reaction);
    ResponseEntity<ResponseObject> deleteReactionOfAnIdea(DeleteReactionRequest deleteRequest);
}
