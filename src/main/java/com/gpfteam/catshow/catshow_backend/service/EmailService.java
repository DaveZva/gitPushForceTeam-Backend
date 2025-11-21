package com.gpfteam.catshow.catshow_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendPasswordResetEmail(String to, String resetLink) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject("Obnovení hesla - CatShow");
            message.setText("Dobrý den,\n\n" +
                    "Byl vyžádán reset hesla pro váš účet.\n" +
                    "Klikněte na tento odkaz pro nastavení nového hesla:\n\n" +
                    resetLink + "\n\n" +
                    "Odkaz je platný 1 hodinu.\n" +
                    "Pokud jste o změnu nežádali, ignorujte tento email.");

            mailSender.send(message);
            System.out.println("Email úspěšně odeslán na: " + to);
        } catch (Exception e) {
            System.err.println("Chyba při odesílání emailu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}