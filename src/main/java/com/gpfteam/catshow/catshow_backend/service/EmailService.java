package com.gpfteam.catshow.catshow_backend.service;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void sendPasswordResetEmail(String to, String resetLink) {
        System.out.println("==================================================");
        System.out.println("ODESÍLÁNÍ EMAILU PRO OBNOVU HESLA");
        System.out.println("Komu: " + to);
        System.out.println("Obsah: Klikněte na následující odkaz pro reset hesla:");
        System.out.println(resetLink);
        System.out.println("==================================================");
    }
}