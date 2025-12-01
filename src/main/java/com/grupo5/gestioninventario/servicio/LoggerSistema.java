package com.grupo5.gestioninventario.servicio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerSistema {
    private static volatile LoggerSistema instance;
    private final Path logFile;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private LoggerSistema() {
        Path dir = Paths.get(System.getProperty("user.home"), "inventario_logs");
        try {
            Files.createDirectories(dir);
        } catch (IOException ignored) {}
        logFile = dir.resolve("app.log");
    }

    public static LoggerSistema getInstance() {
        if (instance == null) {
            synchronized (LoggerSistema.class) {
                if (instance == null) instance = new LoggerSistema();
            }
        }
        return instance;
    }

    public void info(String mensaje) { escribir("INFO", mensaje, null); }
    public void error(String mensaje, Throwable t) { escribir("ERROR", mensaje, t); }

    private synchronized void escribir(String nivel, String mensaje, Throwable t) {
        String ts = LocalDateTime.now().format(fmt);
        StringBuilder sb = new StringBuilder()
            .append(ts).append(" [").append(nivel).append("] ").append(mensaje).append(System.lineSeparator());
        if (t != null) {
            sb.append(t.toString()).append(System.lineSeparator());
        }
        try {
            Files.writeString(logFile, sb.toString(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ignored) {}
        System.out.print(sb.toString());
    }
}

