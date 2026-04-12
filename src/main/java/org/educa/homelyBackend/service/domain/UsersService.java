package org.educa.homelyBackend.service.domain;

import com.resend.core.exception.ResendException;
import org.educa.homelyBackend.entity.UserRoles;
import org.educa.homelyBackend.entity.UserStatuses;
import org.educa.homelyBackend.entity.Users;
import org.educa.homelyBackend.repository.UsersRepository;
import org.educa.homelyBackend.service.common.CloudinaryService;
import org.educa.homelyBackend.service.common.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    private static final Integer AVATAR_SIZE = 500;

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRolesService userRolesService;
    private final UserStatusesService userStatusesService;
    private final EmailService emailService;
    private final CloudinaryService cloudinaryService;

    public UsersService(UsersRepository usersRepository, PasswordEncoder passwordEncoder, UserRolesService userRolesService, UserStatusesService userStatusesService, EmailService emailService, CloudinaryService cloudinaryService) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRolesService = userRolesService;
        this.userStatusesService = userStatusesService;
        this.emailService = emailService;
        this.cloudinaryService = cloudinaryService;
    }

    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    public Optional<Users> findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    @Transactional(rollbackFor = Exception.class)
    public Optional<Users> processNewUser(String name, String email, String password) throws ResendException, IOException {
        Optional<UserRoles> userRol = userRolesService.findByName("USER");
        Optional<UserStatuses> userStatus = userStatusesService.findByName("ACTIVE");

        if (userRol.isEmpty()) return Optional.empty();
        if (userStatus.isEmpty()) return Optional.empty();

        Users newUser = new Users();

        newUser.setIdRole(userRol.get());
        newUser.setIdStatus(userStatus.get());
        newUser.setImageUrl(generateAvatar(name));
        newUser.setName(name);
        newUser.setEmail(email);
        if (password != null && !password.isBlank()) newUser.setHashPassword(encodePassword(password));

        saveUser(newUser);
        emailService.sendWelcomeEmail(email, name);
        return Optional.of(newUser);
    }

    public void saveUser(Users user) {
        usersRepository.save(user);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    private String generateAvatar(String name) throws IOException {
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
        return cloudinaryService.uploadFile(baos.toByteArray());
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