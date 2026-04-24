package org.educa.homelyBackend.services.dedicated;

import org.educa.homelyBackend.daos.ConversationDao;
import org.springframework.stereotype.Service;

@Service
public class ConversationService {

    private final ConversationDao conversationDao;

    public ConversationService(ConversationDao conversationDao) {
        this.conversationDao = conversationDao;
    }
}
