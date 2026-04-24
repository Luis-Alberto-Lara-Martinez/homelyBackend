package org.educa.homelyBackend.services.dedicated;

import org.educa.homelyBackend.daos.ConversationDao;
import org.educa.homelyBackend.models.ConversationModel;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ConversationService {

    private final ConversationDao conversationDao;

    public ConversationService(ConversationDao conversationDao) {
        this.conversationDao = conversationDao;
    }

    public ConversationModel findByIdOrThrow(Integer id) {
        return conversationDao.findById(id)
                .orElseThrow(() -> ExceptionUtil.manageException(
                        HttpStatus.NOT_FOUND,
                        "Conversation not found with id " + id
                ).get());
    }
}
