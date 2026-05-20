package org.educa.homelyBackend.controllers.business;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.PropertyDto;
import org.educa.homelyBackend.dtos.requests.CreatePropertyDtoRequest;
import org.educa.homelyBackend.dtos.requests.FindPropertiesWithinRadiusDtoRequest;
import org.educa.homelyBackend.dtos.requests.PageDtoRequest;
import org.educa.homelyBackend.facades.business.PropertyFacade;
import org.educa.homelyBackend.routes.ConfigurationRoutes;
import org.educa.homelyBackend.utils.ResponseEntityUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyFacade propertyFacade;

    @PostMapping(ConfigurationRoutes.ADMIN + "/properties")
    public Page<PropertyDto> findAllProperties(@Valid @RequestBody PageDtoRequest request) {
        return propertyFacade.findAllProperties(request);
    }

    @PostMapping(ConfigurationRoutes.API + "/property/{id}")
    public PropertyDto findPropertyById(@PathVariable @Valid Integer id) {
        return propertyFacade.findPropertyById(id);
    }

    @PostMapping(ConfigurationRoutes.API + "/properties")
    public List<PropertyDto> findAddressesWithinRadius(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody FindPropertiesWithinRadiusDtoRequest request
    ) {
        return propertyFacade.findPropertiesWithinRadius(
                request.latitude(),
                request.longitude(),
                request.radiusKm()
        );
    }

    @PostMapping(ConfigurationRoutes.ADMIN + "/property")
    public ResponseEntity<Map<String, String>> saveProperty(
            @AuthenticationPrincipal String email,
            @Valid @ModelAttribute CreatePropertyDtoRequest request
    ) {
        propertyFacade.saveProperty(email, request);
        return ResponseEntity.ok(Map.of("message", "Propiedad creada correctamente"));
    }

    @DeleteMapping(ConfigurationRoutes.ADMIN + "/property/{id}")
    public ResponseEntity<Map<String, String>> deleteProperty(@Valid @PathVariable Integer id) {
        propertyFacade.deletePropertyById(id);
        return ResponseEntityUtil.ok("Propiedad eliminada correctamente");
    }
}
