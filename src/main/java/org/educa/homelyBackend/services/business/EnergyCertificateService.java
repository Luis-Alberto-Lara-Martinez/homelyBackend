package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.EnergyCertificateModel;

public interface EnergyCertificateService {
    EnergyCertificateModel findByPropertyIdOrThrow(Integer propertyId);

    EnergyCertificateModel save(EnergyCertificateModel energyCertificateModel);

    EnergyCertificateModel update(Integer propertyId, EnergyCertificateModel energyCertificateModel);
}
