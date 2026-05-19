package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.ResetTokenModel;
import org.educa.homelyBackend.models.UserModel;

public interface ResetTokenService {
    ResetTokenModel findByTokenOrThrow(String token);

    ResetTokenModel save(ResetTokenModel resetToken);

    ResetTokenModel update(String token, ResetTokenModel resetTokenModel);
}
