package org.educa.homelyBackend.controllers.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.requests.FindAddressesWithinRadiusRequest;
import org.educa.homelyBackend.dtos.responses.PropertyResponse;
import org.educa.homelyBackend.facades.api.PropertyAddressApiFacade;
import org.educa.homelyBackend.routes.ConfigurationRoutes;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ConfigurationRoutes.API + "/property")
@RequiredArgsConstructor
public class PropertyAddressApiController {

    private final PropertyAddressApiFacade propertyAddressApiFacade;

    @PostMapping("/all")
    public List<PropertyResponse> findAddressesWithinRadius(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody FindAddressesWithinRadiusRequest request
    ) {
        return propertyAddressApiFacade.findAddressesWithinRadius(
                request.latitude(),
                request.longitude(),
                request.radiusKm()
        );
    }
}