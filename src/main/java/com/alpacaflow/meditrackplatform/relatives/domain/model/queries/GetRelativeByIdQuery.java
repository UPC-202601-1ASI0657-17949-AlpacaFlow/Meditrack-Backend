package com.alpacaflow.meditrackplatform.relatives.domain.model.queries;

public record GetRelativeByIdQuery(Long id) {
    public GetRelativeByIdQuery {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Relative id must be a positive number");
        }
    }
}
