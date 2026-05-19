package org.educa.homelyBackend.controllers.business;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.requests.ContactEmailDtoRequest;
import org.educa.homelyBackend.dtos.requests.WorkWithUsEmailDtoRequest;
import org.educa.homelyBackend.facades.business.ResendFacade;
import org.educa.homelyBackend.routes.ConfigurationRoutes;
import org.educa.homelyBackend.utils.ResponseEntityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ResendController {

    private final ResendFacade resendFacade;

    @PostMapping(ConfigurationRoutes.API + "/work-with-us")
    public ResponseEntity<Map<String, String>> sendWorkWithUsEmail(@Valid @ModelAttribute WorkWithUsEmailDtoRequest request) {
        resendFacade.sendWorkWithUsEmail(request);
        return ResponseEntityUtil.ok("Solicitud de empleo enviada correctamente");
    }

    @PostMapping(ConfigurationRoutes.API + "/contact")
    public ResponseEntity<Map<String, String>> sendContactEmail(@Valid @RequestBody ContactEmailDtoRequest request) {
        resendFacade.sendContactEmail(request);
        return ResponseEntityUtil.ok("Correo de contacto enviado correctamente");
    }
}
