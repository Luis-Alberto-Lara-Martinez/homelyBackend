package org.educa.homelyBackend.controllers.chat;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.services.business.MessageService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;

    @MessageMapping("/chat/{conversationId}")
    public void processMessage(@DestinationVariable Integer conversationId, String content) {
        messageService.saveAndBroadcast(conversationId, content);
    }
}
