package org.educa.homelyBackend.services.common;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.Year;
import java.util.Map;

@Service
@Validated
public class EmailService {

    private static final String FROM_EMAIL = "Homely <comunications@homelyweb.app>";
    private static final String BASE_FRONTEND_URL = "https://homelyweb.app";

    private final Resend resend;
    private final TemplateEngine templateEngine;

    public EmailService(
            @Value("${resend.api.key}") String apiKey,
            TemplateEngine templateEngine
    ) {
        this.resend = new Resend(apiKey);
        this.templateEngine = templateEngine;
    }

    public void sendWelcomeEmail(
            @NotBlank(message = "El correo no puede estar vacío")
            @Email(message = "Formato de email no válido")
            String to,

            @NotBlank(message = "El nombre no puede estar vacío")
            String name
    ) {
        String subject = "¡Bienvenido a Homely, %s!".formatted(name);
        String templateName = "welcome";

        sendEmail(to, subject, name, templateName, null);
    }

    public void sendResetPasswordEmail(
            @NotBlank(message = "El correo no puede estar vacío")
            @Email(message = "Formato de email no válido")
            String to,

            @NotBlank(message = "El nombre no puede estar vacío")
            String name,

            @NotNull(message = "Los minutos de expiración no pueden ser nulos")
            Integer expirationMinutes,

            @NotBlank(message = "El token de reseteo no puede estar vacío")
            String resetToken
    ) {
        String subject = "Restablece tu contraseña de Homely";
        String templateName = "reset-password";
        String resetLink = BASE_FRONTEND_URL + "/reset-password?token=" + resetToken;

        Map<String, Object> extraVariables = Map.of(
                "resetLink", resetLink,
                "expirationTime", String.format("%d minutos", expirationMinutes)
        );

        sendEmail(to, subject, name, templateName, extraVariables);
    }

    private void sendEmail(
            String to,
            String subject,
            String name,
            String templateName,
            Map<String, Object> extraVariables
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
            throw ExceptionUtil.manageException(e, HttpStatus.BAD_REQUEST, "Error sending email by Resend");
        }
    }
}
