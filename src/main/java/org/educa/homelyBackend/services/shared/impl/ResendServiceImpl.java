package org.educa.homelyBackend.services.shared.impl;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.services.shared.ResendService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.jsoup.Jsoup;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.Clock;
import java.time.Year;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResendServiceImpl implements ResendService {

    private static final String FROM_EMAIL = "Homely <comunications@homelyweb.app>";
    private static final String BASE_FRONTEND_URL = "https://homelyweb.app";

    private final Resend resend;
    private final TemplateEngine templateEngine;
    private final Clock clock;

    @Override
    public void sendWelcomeEmail(String to, String name) {
        sendEmail(to, "¡Bienvenido a Homely, " + name, name, "welcome", null);
    }

    @Override
    public void sendResetPasswordEmail(String to, String name, String resetToken, Integer expirationMinutes) {
        String resetLink = BASE_FRONTEND_URL + "/reset-password?token=" + resetToken;

        Map<String, Object> extraVariables = Map.of(
                "resetLink", resetLink,
                "expirationMinutes", expirationMinutes
        );

        sendEmail(to, "Restablece tu contraseña de Homely", name, "reset-password", extraVariables);
    }

    private void sendEmail(
            String to, String subject, String name, String templateName, Map<String, Object> extraVariables
    ) {
        String html = templateEngine.process(templateName, createContext(name, extraVariables));

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(FROM_EMAIL)
                .to(to)
                .subject(subject)
                .html(html)
                .text(Jsoup.parse(html).text())
                .build();

        try {
            resend.emails().send(params);
        } catch (ResendException e) {
            throw ExceptionUtil.manageException(
                    e, HttpStatus.BAD_REQUEST, "Error de Resend al enviar un correo electrónico a " + to
            );
        }
    }

    private Context createContext(String name, Map<String, Object> extraVariables) {
        Context context = new Context();

        if (extraVariables != null && !extraVariables.isEmpty()) {
            context.setVariables(extraVariables);
        }

        context.setVariable("name", name);
        context.setVariable("year", Year.now(clock).getValue());

        return context;
    }
}
