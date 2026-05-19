package org.educa.homelyBackend.services.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.ResidenceDao;
import org.educa.homelyBackend.models.ResidenceModel;
import org.educa.homelyBackend.services.business.ResidenceService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResidenceServiceImpl implements ResidenceService {

    private final ResidenceDao residenceDao;

    @Override
    public ResidenceModel findByPropertyIdOrThrow(Integer propertyId) {
        return residenceDao.findById(propertyId)
                .orElseThrow(() -> ExceptionUtil.manageException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró ninguna residencia con el ID proporcionado"
                ).get());
    }

    @Override
    public ResidenceModel save(ResidenceModel residenceModel) {
        return residenceDao.save(residenceModel);
    }

    @Override
    public ResidenceModel update(ResidenceModel residenceModel) {
        boolean makeChanges = false;
        ResidenceModel existingResidence = findByPropertyIdOrThrow(residenceModel.getId());

        if (residenceModel.getBedrooms() != null) {
            existingResidence.setBedrooms(residenceModel.getBedrooms());
            makeChanges = true;
        }

        if (residenceModel.getBathrooms() != null) {
            existingResidence.setBathrooms(residenceModel.getBathrooms());
            makeChanges = true;
        }

        if (residenceModel.getConservation() != null) {
            existingResidence.setConservation(residenceModel.getConservation());
            makeChanges = true;
        }

        if (residenceModel.getOrientation() != null) {
            existingResidence.setOrientation(residenceModel.getOrientation());
            makeChanges = true;
        }

        if (makeChanges) {
            return save(existingResidence);
        }

        return existingResidence;
    }
}
