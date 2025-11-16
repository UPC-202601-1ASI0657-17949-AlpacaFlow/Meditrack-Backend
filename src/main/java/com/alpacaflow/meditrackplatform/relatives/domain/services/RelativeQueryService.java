package com.alpacaflow.meditrackplatform.relatives.domain.services;

import com.alpacaflow.meditrackplatform.relatives.domain.model.aggregates.Relative;
import com.alpacaflow.meditrackplatform.relatives.domain.model.queries.GetRelativeByIdQuery;
import com.alpacaflow.meditrackplatform.relatives.domain.model.queries.GetRelativeByUserIdQuery;

import java.util.Optional;

public interface RelativeQueryService {
    /**
     * Handle the GetRelativeByIdQuery to retrieve a Relative by its ID.
     * @param query The get relative by ID query.
     * @return The Relative
     * @see GetRelativeByIdQuery
     */
    Optional<Relative> handle(GetRelativeByIdQuery query);

    /**
     * Handle the GetRelativeByUserIdQuery to retrieve a Relative by its userId.
     * @param query The get relative by userId query.
     * @return The Relative
     * @see GetRelativeByUserIdQuery
     */
    Optional<Relative> handle(GetRelativeByUserIdQuery query);
}
