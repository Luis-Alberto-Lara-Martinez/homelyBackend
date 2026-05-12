package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.ResidenceModel;

public interface ResidenceService {
    ResidenceModel findByIdOrThrow(Integer propertyId);
}
