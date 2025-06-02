package org.example.multithreading;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerService {
    private static final String FILE_NAME = "log.txt";

    public static void log(String operationName, String level, long durationMs) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        String entry = String.format("[%s] [%s] Operation: %s, Duration: %d ms%n", timestamp, level, operationName, durationMs);

        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write(entry);
        } catch (IOException e) {
            System.err.println("Log write error: " + e.getMessage());
        }
    }

    public static void logError(String operationName, Exception ex) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        String entry = String.format("[%s] [ERROR] Operation: %s, Exception: %s%n", timestamp, operationName, ex.getMessage());

        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write(entry);
        } catch (IOException e) {
            System.err.println("Log write error: " + e.getMessage());
        }
    }
}
