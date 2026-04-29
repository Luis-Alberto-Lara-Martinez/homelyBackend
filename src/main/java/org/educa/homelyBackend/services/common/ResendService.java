package org.educa.homelyBackend.services.common;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.jsoup.Jsoup;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.Year;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResendService {

    private static final String FROM_EMAIL = "Homely <comunications@homelyweb.app>";
    private static final String BASE_FRONTEND_URL = "https://homelyweb.app";

    private final Resend resend;
    private final TemplateEngine templateEngine;

    public void sendWelcomeEmail(String to, String name) {
        sendEmail(to, "¡Bienvenido a Homely, " + name, name, "welcome", null);
    }

    public void sendResetPasswordEmail(String to, String name, Integer expirationMinutes, String resetToken) {
        String resetLink = BASE_FRONTEND_URL + "/reset-password?token=" + resetToken;

        Map<String, Object> extraVariables = Map.of(
                "resetLink", resetLink,
                "expirationTime", expirationMinutes + " minutos"
        );

        sendEmail(to, "Restablece tu contraseña de Homely", name, "reset-password", extraVariables);
    }

    private void sendEmail(
            String to, String subject, String name, String templateName, Map<String, Object> extraVariables
    ) {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("year", Year.now().getValue());

        if (extraVariables != null && !extraVariables.isEmpty()) {
            context.setVariables(extraVariables);
        }

        String html = templateEngine.process(templateName, context);

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
            throw ExceptionUtil.manageException(e, HttpStatus.BAD_REQUEST, "Error de Resend al enviar un correo electrónico a " + to);
        }
    }
}
