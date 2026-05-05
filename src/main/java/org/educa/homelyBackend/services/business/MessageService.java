package org.educa.homelyBackend.services.business;

public interface MessageService {
    void saveAndBroadcast(Integer conversationId, String content);
}
