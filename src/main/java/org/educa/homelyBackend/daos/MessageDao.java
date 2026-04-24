package org.educa.homelyBackend.daos;

import org.educa.homelyBackend.models.MessageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageDao extends JpaRepository<MessageModel, Integer> {
}
