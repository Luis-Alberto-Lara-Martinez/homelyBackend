package org.educa.homelyBackend.services.common;

import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class AvatarService {

    private static final Integer AVATAR_SIZE = 500;
    private static final String FONT_NAME = "Arial";

    public byte[] generateAvatar(String name) {
        String initials = obtainInitials(name);

        BufferedImage avatarImage = new BufferedImage(AVATAR_SIZE, AVATAR_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = avatarImage.createGraphics();

        try {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            Color backgroundColor = generateRandomColor();
            graphics.setColor(backgroundColor);
            graphics.fillOval(0, 0, AVATAR_SIZE, AVATAR_SIZE);

            graphics.setColor(getTextColorBasedOnBackground(backgroundColor));
            setupOptimalFont(graphics, initials);

            FontMetrics fm = graphics.getFontMetrics();
            int x = (AVATAR_SIZE - fm.stringWidth(initials)) / 2;
            int y = (AVATAR_SIZE - fm.getHeight()) / 2 + fm.getAscent();

            graphics.drawString(initials, x, y);
        } finally {
            graphics.dispose();
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(avatarImage, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw ExceptionUtil.manageException(e, HttpStatus.BAD_REQUEST, "Error al generar el avatar");
        }
    }

    private String obtainInitials(String name) {
        if (name == null || name.isBlank()) {
            return "??";
        }

        String[] nameParts = name.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < Math.min(nameParts.length, 2); i++) {
            if (!nameParts[i].isBlank()) {
                sb.append(Character.toUpperCase(nameParts[i].charAt(0)));
            }
        }

        return sb.toString();
    }

    private Color generateRandomColor() {
        int r = 50 + (int) (Math.random() * 156);
        int g = 50 + (int) (Math.random() * 156);
        int b = 50 + (int) (Math.random() * 156);
        return new Color(r, g, b);
    }

    private Color getTextColorBasedOnBackground(Color backgroundColor) {
        double brightness = (backgroundColor.getRed() * 0.299 +
                backgroundColor.getGreen() * 0.587 +
                backgroundColor.getBlue() * 0.114);
        return brightness > 150
                ? Color.BLACK
                : Color.WHITE;
    }

    private void setupOptimalFont(Graphics2D graphics, String initials) {
        int fontSize = AVATAR_SIZE / 2;
        graphics.setFont(new Font(FONT_NAME, Font.BOLD, fontSize));
        FontMetrics fm = graphics.getFontMetrics();

        if (fm.stringWidth(initials) > AVATAR_SIZE * 0.8) {
            fontSize = (int) (fontSize * (AVATAR_SIZE * 0.8 / fm.stringWidth(initials)));
            graphics.setFont(new Font(FONT_NAME, Font.BOLD, fontSize));
        }
    }
}
