package org.educa.homelyBackend.daos;

import org.educa.homelyBackend.models.ConversationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationDao extends JpaRepository<ConversationModel, Integer> {
}
