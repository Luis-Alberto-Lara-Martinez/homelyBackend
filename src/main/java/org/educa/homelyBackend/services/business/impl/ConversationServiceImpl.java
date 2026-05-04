package org.educa.homelyBackend.services.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.ConversationDao;
import org.educa.homelyBackend.models.ConversationModel;
import org.educa.homelyBackend.services.business.ConversationService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationDao conversationDao;

    @Override
    public ConversationModel findByIdOrThrow(Integer id) {
        return conversationDao.findById(id)
                .orElseThrow(() -> ExceptionUtil.manageException(
                        HttpStatus.NOT_FOUND,
                        "No existe ninguna conversación con el id " + id
                ).get());
    }
}
