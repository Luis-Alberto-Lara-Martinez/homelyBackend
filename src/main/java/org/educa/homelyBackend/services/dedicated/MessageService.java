package org.educa.homelyBackend.services.dedicated;

import org.educa.homelyBackend.daos.MessageDao;
import org.educa.homelyBackend.models.MessageModel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageService {

    private final MessageDao messageDao;
    private final SimpMessagingTemplate messagingTemplate;
    private final ConversationService conversationService;

    public MessageService(MessageDao messageDao, SimpMessagingTemplate messagingTemplate, ConversationService conversationService) {
        this.messageDao = messageDao;
        this.messagingTemplate = messagingTemplate;
        this.conversationService = conversationService;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveAndBroadcast(Integer conversationId, String content) {
        MessageModel message = new MessageModel();
        message.setConversation(conversationService.findByIdOrThrow(conversationId));
        message.setContent(content);

        messageDao.save(message);

        messagingTemplate.convertAndSend("/topic/messages/" + conversationId, message);
    }
}
