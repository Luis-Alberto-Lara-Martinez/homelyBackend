package org.educa.homelyBackend.service.common;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class AvatarService {

    private static final Integer AVATAR_SIZE = 500;

    public byte[] generateAvatar(String name) throws IOException {
        String initials = obtainInitials(name);

        BufferedImage avatarImage = new BufferedImage(AVATAR_SIZE, AVATAR_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = avatarImage.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color backgroundColor = generateRandomColor();
        graphics.setColor(backgroundColor);
        graphics.fillOval(0, 0, AVATAR_SIZE, AVATAR_SIZE);

        Color textColor = getTextColorBasedOnBackground(backgroundColor);
        graphics.setColor(textColor);

        int fontSize = AVATAR_SIZE / 2;
        Font font = new Font("Arial", Font.BOLD, fontSize);
        graphics.setFont(font);

        FontMetrics fm = graphics.getFontMetrics();
        while (fm.stringWidth(initials) > AVATAR_SIZE * 0.8) {
            fontSize--;
            font = new Font("Arial", Font.BOLD, fontSize);
            graphics.setFont(font);
            fm = graphics.getFontMetrics();
        }

        int x = (AVATAR_SIZE - fm.stringWidth(initials)) / 2;
        int y = (AVATAR_SIZE - fm.getHeight()) / 2 + fm.getAscent();

        graphics.drawString(initials, x, y);
        graphics.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(avatarImage, "png", baos);
        return baos.toByteArray();
    }

    private String obtainInitials(String name) {
        String[] parts = name.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (!part.isBlank()) {
                sb.append(Character.toUpperCase(part.charAt(0)));
            }
        }
        return sb.toString().toUpperCase();
    }

    private Color generateRandomColor() {
        int r = 50 + (int) (Math.random() * 156);
        int g = 50 + (int) (Math.random() * 156);
        int b = 50 + (int) (Math.random() * 156);
        return new Color(r, g, b);
    }

    private Color getTextColorBasedOnBackground(Color backgroundColor) {
        double brightness = ((backgroundColor.getRed() * 0.299) +
                (backgroundColor.getGreen() * 0.587) +
                (backgroundColor.getBlue() * 0.114));
        return brightness > 150
                ? Color.BLACK
                : Color.WHITE;
    }
}