package com.project.web.payload.response;

import lombok.Data;

@Data
public class ReactionResponse {
    Long reactionId;
    String username;
    String reactionType;

    public ReactionResponse() {
    }
}
