package org.watermelon;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    public static void info(String message) {
        Path log = Paths.get("application.log");
        try (FileWriter writer = new FileWriter(log.toFile(), true)) {
            LocalDateTime label = LocalDateTime.now();
            writer.write(label.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + " [DEBUG] " + message + "\n");
            writer.flush();
        } catch (IOException e) {
            System.out.println("It is failed to write message to log file!");
        }
    }

    public static void error(String message) {
        Path log = Paths.get("application.log");
        try (FileWriter writer = new FileWriter(log.toFile(), true)) {
            LocalDateTime label = LocalDateTime.now();
            writer.write(label.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + " [ERROR] " + message + "\n");
            writer.flush();
        } catch (IOException e) {
            System.out.println("It is failed to write error message to log file!");
        }
    }
}

