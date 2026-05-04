package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.ConversationModel;

public interface ConversationService {
    ConversationModel findByIdOrThrow(Integer id);
}
