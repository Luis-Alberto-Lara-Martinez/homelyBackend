package org.educa.homelyBackend.facades.business;

import org.educa.homelyBackend.dtos.requests.ContactEmailDtoRequest;
import org.educa.homelyBackend.dtos.requests.WorkWithUsEmailDtoRequest;

public interface ResendFacade {
    void sendWorkWithUsEmail(WorkWithUsEmailDtoRequest request);

    void sendContactEmail(ContactEmailDtoRequest request);
}
