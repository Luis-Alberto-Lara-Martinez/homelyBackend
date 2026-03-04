package org.educa.homelyBackend.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.Year;

@Service
public class EmailService {

    private static final String FROM_EMAIL = "Homely <comunications@homelyweb.app>";
    private static final int ACTUAL_YEAR = Year.now().getValue();

    private final Resend resend;
    private final TemplateEngine templateEngine;

    public EmailService(
            @Value("${resend.api.key}") String apiKey,
            TemplateEngine templateEngine) {
        this.resend = new Resend(apiKey);
        this.templateEngine = templateEngine;
    }

    public void sendWelcomeEmail(String to, String name) throws ResendException {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("year", ACTUAL_YEAR);

        sendEmail(
                to,
                "¡Bienvenido a Homely, %s!".formatted(name),
                "welcome",
                context
        );
    }

    public void sendResetPasswordEmail(
            String to,
            String name,
            String resetLink,
            String expirationTime)
            throws ResendException {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("resetLink", resetLink);
        context.setVariable("expirationTime", expirationTime);
        context.setVariable("year", ACTUAL_YEAR);

        sendEmail(
                to,
                "Restablece tu contraseña de Homely",
                "reset-password",
                context);
    }

    private void sendEmail(
            String to,
            String subject,
            String templateName,
            Context context)
            throws ResendException {
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