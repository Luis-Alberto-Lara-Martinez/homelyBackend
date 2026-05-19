package org.educa.homelyBackend.facades.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.requests.ContactEmailDtoRequest;
import org.educa.homelyBackend.dtos.requests.WorkWithUsEmailDtoRequest;
import org.educa.homelyBackend.facades.business.ResendFacade;
import org.educa.homelyBackend.services.shared.ResendService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResendFacadeImpl implements ResendFacade {

    private final ResendService resendService;

    @Override
    public void sendWorkWithUsEmail(WorkWithUsEmailDtoRequest request) {
        resendService.sendWorkWithUsEmail(
                request.from(),
                request.name(),
                request.workingArea(),
                request.phone(),
                request.description(),
                request.cvFile()
        );
    }

    @Override
    public void sendContactEmail(ContactEmailDtoRequest request) {
        resendService.sendContactEmail(
                request.name(),
                request.email(),
                request.message()
        );
    }
}
