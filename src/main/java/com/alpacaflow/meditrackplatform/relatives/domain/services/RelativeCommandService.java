package com.alpacaflow.meditrackplatform.relatives.domain.services;

import com.alpacaflow.meditrackplatform.relatives.domain.model.aggregates.Relative;
import com.alpacaflow.meditrackplatform.relatives.domain.model.commands.CreateRelativeCommand;

/**
 * Service for handling relative commands.
 */
public interface RelativeCommandService {
    /**
     * Handle the CreateRelativeCommand to create a new Relative.
     * @param command The create relative command.
     * @return The created Relative
     * @see CreateRelativeCommand
     */
    Relative handle(CreateRelativeCommand command);
}

