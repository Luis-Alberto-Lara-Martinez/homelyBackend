package org.educa.homelyBackend.services.shared.impl;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.Attachment;
import com.resend.services.emails.model.CreateEmailOptions;
import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.properties.ResendProperties;
import org.educa.homelyBackend.services.shared.ResendService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.jsoup.Jsoup;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.time.Clock;
import java.time.Year;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResendServiceImpl implements ResendService {

    private final Resend resend;
    private final TemplateEngine templateEngine;
    private final Clock clock;

    @Override
    public void sendWelcomeEmail(String to, String name) {
        sendEmail(ResendProperties.PRIMARY_EMAIL, to, "¡Bienvenido a Homely, " + name + "!", name, "welcome", null);
    }

    @Override
    public void sendResetPasswordEmail(String to, String name, String resetToken, Integer expirationMinutes) {
        String resetLink = ResendProperties.BASE_FRONTEND_URL + "/reset-password?token=" + resetToken;

        Map<String, Object> extraVariables = Map.of(
                "resetLink", resetLink,
                "expirationMinutes", expirationMinutes
        );

        sendEmail(ResendProperties.PRIMARY_EMAIL, to, "Restablece tu contraseña de Homely", name, "reset-password", extraVariables);
    }

    @Override
    public void sendWorkWithUsEmail(String from, String name, String workingArea, String phone, String description, MultipartFile cvFile) {
        Map<String, Object> variables = Map.of(
                "from", from,
                "name", name,
                "workingArea", workingArea,
                "phone", phone,
                "description", description
        );

        sendEmail(ResendProperties.SECONDARY_EMAIL, ResendProperties.PRIMARY_EMAIL, "Nueva Solicitud de Empleo: " + name, name, "work-with-us", variables, cvFile);
    }

    @Override
    public void sendContactEmail(String name, String email, String message) {
        Map<String, Object> variables = Map.of(
                "name", name,
                "email", email,
                "message", message
        );

        sendEmail(ResendProperties.SECONDARY_EMAIL, ResendProperties.PRIMARY_EMAIL, "Nuevo Mensaje de Contacto de " + name, name, "contact", variables);
    }

    private void sendEmail(
            String from, String to, String subject, String name, String templateName, Map<String, Object> extraVariables
    ) {
        sendEmail(from, to, subject, name, templateName, extraVariables, null);
    }

    private void sendEmail(
            String from, String to, String subject, String name, String templateName, Map<String, Object> extraVariables, MultipartFile attachmentFile
    ) {
        String html = templateEngine.process(templateName, createContext(name, extraVariables));

        CreateEmailOptions.Builder paramsBuilder = CreateEmailOptions.builder()
                .from(from)
                .to(to)
                .subject(subject)
                .html(html)
                .text(Jsoup.parse(html).text());

        if (attachmentFile != null && !attachmentFile.isEmpty()) {
            try {
                Attachment attachment = Attachment.builder()
                        .fileName(attachmentFile.getOriginalFilename())
                        .content(Base64.getEncoder().encodeToString(attachmentFile.getBytes()))
                        .build();

                paramsBuilder.attachments(attachment);
            } catch (IOException e) {
                throw ExceptionUtil.manageException(
                        e, HttpStatus.INTERNAL_SERVER_ERROR, "Error al procesar el archivo adjunto para el correo destinado a " + to
                );
            }
        }

        try {
            resend.emails().send(paramsBuilder.build());
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
