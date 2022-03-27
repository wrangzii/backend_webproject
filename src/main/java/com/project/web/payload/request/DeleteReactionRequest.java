package com.project.web.payload.request;

import lombok.Data;

@Data
public class DeleteReactionRequest {
    Long userId;
    Long ideaId;
}
