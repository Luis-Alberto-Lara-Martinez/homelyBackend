package org.educa.homelyBackend.daos;

import org.educa.homelyBackend.models.EnergyCertificateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnergyCertificateDao extends JpaRepository<EnergyCertificateModel, Integer> {
}
