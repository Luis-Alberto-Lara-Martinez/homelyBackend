package org.educa.homelyBackend.controllers.chat;

import org.educa.homelyBackend.services.dedicated.MessageService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final MessageService messageService;

    public ChatController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/chat/{conversationId}")
    public void processMessage(@DestinationVariable Integer conversationId, String content) {
        messageService.saveAndBroadcast(conversationId, content);
    }
}
