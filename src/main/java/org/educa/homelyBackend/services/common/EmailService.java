package org.educa.homelyBackend.services.common;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
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
    ) throws ResendException {
        sendEmail(
                name,
                to,
                "¡Bienvenido a Homely, %s!".formatted(name),
                "welcome",
                null
        );
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
    ) throws ResendException {
        String resetLink = BASE_FRONTEND_URL + "/reset-password?token=" + resetToken + "&email=" + to;

        Map<String, Object> variables = Map.of(
                "resetLink", resetLink,
                "expirationTime", String.format("%d minutos", expirationMinutes)
        );

        sendEmail(
                name,
                to,
                "Restablece tu contraseña de Homely",
                "reset-password",
                variables
        );
    }

    private void sendEmail(
            String name,
            String to,
            String subject,
            String templateName,
            Map<String, Object> variables
    ) throws ResendException {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("year", Year.now().getValue());

        if (variables != null && !variables.isEmpty()) context.setVariables(variables);

        String html = templateEngine.process(templateName, context);

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(FROM_EMAIL)
                .to(to)
                .subject(subject)
                .html(html)
                .text(Jsoup.parse(html).text())
                .build();

        resend.emails().send(params);
    }
}