package org.educa.homelyBackend.controllers.business;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.FavouriteDto;
import org.educa.homelyBackend.dtos.requests.SaveFavouriteDtoRequest;
import org.educa.homelyBackend.facades.business.FavouriteFacade;
import org.educa.homelyBackend.routes.ConfigurationRoutes;
import org.educa.homelyBackend.utils.ResponseEntityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(ConfigurationRoutes.API + "/favourites")
@RequiredArgsConstructor
public class FavouriteController {

    private final FavouriteFacade favouriteFacade;

    @GetMapping("")
    public List<FavouriteDto> findAllByUser(@AuthenticationPrincipal String email) {
        return favouriteFacade.findAllByUser(email);
    }

    @PostMapping("")
    public ResponseEntity<Map<String, String>> save(@AuthenticationPrincipal String email, @Valid @RequestBody SaveFavouriteDtoRequest request) {
        favouriteFacade.save(email, request.propertyId());
        return ResponseEntityUtil.ok("Guardado como favorito");
    }

    @DeleteMapping("")
    public ResponseEntity<Map<String, String>> delete(@AuthenticationPrincipal String email, @Valid @RequestBody SaveFavouriteDtoRequest request) {
        favouriteFacade.delete(email, request.propertyId());
        return ResponseEntityUtil.ok("Eliminado de favoritos");
    }
}

