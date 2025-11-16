package com.alpacaflow.meditrackplatform.relatives.domain.model.queries;

public record GetRelativeByUserIdQuery(Long userId) {
    public GetRelativeByUserIdQuery {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User id must be a positive number");
        }
    }
}

