package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.ResidenceModel;

public interface ResidenceService {
    ResidenceModel findByPropertyIdOrThrow(Integer propertyId);

    ResidenceModel save(ResidenceModel residenceModel);

    ResidenceModel update(ResidenceModel residenceModel);
}
