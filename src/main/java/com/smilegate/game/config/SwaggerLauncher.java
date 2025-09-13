package com.smilegate.game.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SwaggerLauncher {

    @Value("${swagger.host-url}")
    private String hostUrl;

    private static final String SWAGGER_PATH = "/swagger-ui/index.html";

    @EventListener(ApplicationReadyEvent.class)
    public void launchBrowser() {
        String swaggerUrl = hostUrl + SWAGGER_PATH;
        String os = System.getProperty("os.name").toLowerCase();

        try {
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "start", swaggerUrl).start();
            } else if (os.contains("mac")) {
                new ProcessBuilder("open", swaggerUrl).start();
            } else if (os.contains("nix") || os.contains("nux")) {
                new ProcessBuilder("xdg-open", swaggerUrl).start();
            } else {
                System.out.println("⚠️ Unknown OS — please open Swagger manually: " + swaggerUrl);
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to open Swagger UI: " + e.getMessage());
        }
    }
}
