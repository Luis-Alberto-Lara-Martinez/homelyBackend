package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.EnergyCertificateModel;

public interface EnergyCertificateService {
    EnergyCertificateModel findByPropertyId(Integer propertyId);

    EnergyCertificateModel save(EnergyCertificateModel energyCertificateModel);

    void delete(EnergyCertificateModel energyCertificateModel);
}
