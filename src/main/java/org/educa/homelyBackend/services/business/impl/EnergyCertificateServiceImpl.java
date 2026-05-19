package org.educa.homelyBackend.services.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.EnergyCertificateDao;
import org.educa.homelyBackend.models.EnergyCertificateModel;
import org.educa.homelyBackend.services.business.EnergyCertificateService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnergyCertificateServiceImpl implements EnergyCertificateService {

    private final EnergyCertificateDao energyCertificateDao;

    @Override
    public EnergyCertificateModel findByPropertyIdOrThrow(Integer propertyId) {
        return energyCertificateDao.findById(propertyId)
                .orElseThrow(() -> ExceptionUtil.manageException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el certificado energético para la propiedad con ID: " + propertyId
                ).get());
    }

    @Override
    public EnergyCertificateModel save(EnergyCertificateModel energyCertificateModel) {
        return energyCertificateDao.save(energyCertificateModel);
    }

    @Override
    public EnergyCertificateModel update(Integer propertyId, EnergyCertificateModel energyCertificate) {
        boolean makeChanges = false;
        EnergyCertificateModel existingEnergyCertificate = findByPropertyIdOrThrow(propertyId);

        if (energyCertificate.getHasCertificate() != null) {
            existingEnergyCertificate.setHasCertificate(energyCertificate.getHasCertificate());
            makeChanges = true;
        }

        if (energyCertificate.getConsumptionScale() != null) {
            existingEnergyCertificate.setConsumptionScale(energyCertificate.getConsumptionScale());
            makeChanges = true;
        }

        if (energyCertificate.getConsumptionValue() != null) {
            existingEnergyCertificate.setConsumptionValue(energyCertificate.getConsumptionValue());
            makeChanges = true;
        }

        if (energyCertificate.getEmissionsScale() != null) {
            existingEnergyCertificate.setEmissionsScale(energyCertificate.getEmissionsScale());
            makeChanges = true;
        }

        if (energyCertificate.getEmissionsValue() != null) {
            existingEnergyCertificate.setEmissionsValue(energyCertificate.getEmissionsValue());
            makeChanges = true;
        }

        if (makeChanges) {
            return save(existingEnergyCertificate);
        }

        return existingEnergyCertificate;
    }
}
