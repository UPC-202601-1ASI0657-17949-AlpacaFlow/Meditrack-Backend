package com.alpacaflow.meditrackplatform.relatives.infrastructure.persistence.repositories;

import com.alpacaflow.meditrackplatform.relatives.domain.model.aggregates.Relative;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RelativeRepository extends JpaRepository<Relative, Long> {

    /**
     * This method is used to find a Relative by its ID.
     * @param id The ID of the Relative.
     * @return The Relative with a specified ID.
     */
    Optional<Relative> findById(Long id);

    /**
     * This method is used to find a Relative by its userId.
     * @param userId The userId of the Relative.
     * @return The Relative with a specified userId.
     */
    Optional<Relative> findByUserId(Long userId);
}
