package com.project.web.payload.request;

import lombok.Data;

@Data
public class ReactionRequest {
    String reactionType;
    Long userId;
    Long ideaId;
}
