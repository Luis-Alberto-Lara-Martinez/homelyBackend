package org.educa.homelyBackend.services.business;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.MessageDao;
import org.educa.homelyBackend.models.MessageModel;
import org.educa.homelyBackend.services.business.impl.ConversationServiceImpl;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageDao messageDao;
    private final SimpMessagingTemplate messagingTemplate;
    private final ConversationServiceImpl conversationServiceImpl;

    @Transactional(rollbackFor = Exception.class)
    public void saveAndBroadcast(Integer conversationId, String content) {
        MessageModel message = new MessageModel();
        message.setConversation(conversationServiceImpl.findByIdOrThrow(conversationId));
        message.setContent(content);

        messageDao.save(message);

        messagingTemplate.convertAndSend("/topic/messages/" + conversationId, message);
    }
}
