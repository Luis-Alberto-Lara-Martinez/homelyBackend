package org.educa.homelyBackend.services.dedicated;

import org.educa.homelyBackend.daos.MessageDao;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final MessageDao messageDao;

    public MessageService(MessageDao messageDao) {
        this.messageDao = messageDao;
    }
}
