package com.alpacaflow.meditrackplatform.relatives.application.internal.queryservices;

import com.alpacaflow.meditrackplatform.relatives.domain.model.aggregates.Relative;
import com.alpacaflow.meditrackplatform.relatives.domain.model.queries.GetRelativeByIdQuery;
import com.alpacaflow.meditrackplatform.relatives.domain.model.queries.GetRelativeByUserIdQuery;
import com.alpacaflow.meditrackplatform.relatives.domain.services.RelativeQueryService;
import com.alpacaflow.meditrackplatform.relatives.infrastructure.persistence.repositories.RelativeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RelativeQueryServiceImpl implements RelativeQueryService {
    private final RelativeRepository relativeRepository;

    public RelativeQueryServiceImpl(RelativeRepository relativeRepository) {
        this.relativeRepository = relativeRepository;
    }

    @Override
    public Optional<Relative> handle(GetRelativeByIdQuery query) {
        return relativeRepository.findById(query.id());
    }

    @Override
    public Optional<Relative> handle(GetRelativeByUserIdQuery query) {
        return relativeRepository.findByUserId(query.userId());
    }
}
