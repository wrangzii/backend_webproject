package com.project.web.payload.response;

import lombok.Data;

@Data
public class ReactionResponse {
    String username;
    String reactionType;

    public ReactionResponse() {
    }
}
