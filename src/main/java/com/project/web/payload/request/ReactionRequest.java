package com.project.web.payload.request;

import lombok.Data;

@Data
public class ReactionRequest {
    Long reactionId;
    String reactionType;
    Long userId;
    Long ideaId;
}
