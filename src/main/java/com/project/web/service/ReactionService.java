package com.project.web.service;

import com.project.web.payload.request.DeleteReactionRequest;
import com.project.web.payload.request.ReactionRequest;
import com.project.web.payload.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface ReactionService {
    ResponseEntity<ResponseObject> getReactionOfAnIdea(Long ideaId);
    void addReactionOfAnIdea(ReactionRequest reaction);
    void editReactionOfAnIdea(ReactionRequest reaction);
    void deleteReactionOfAnIdea(DeleteReactionRequest deleteRequest);
}
