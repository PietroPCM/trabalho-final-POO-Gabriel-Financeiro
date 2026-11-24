package br.com.pietro.financeiro.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppLogger {

    private static volatile AppLogger instance;

    private AppLogger() {
    }

    public static AppLogger getInstance() {
        if (instance == null) {
            synchronized (AppLogger.class) {
                if (instance == null) {
                    instance = new AppLogger();
                }
            }
        }

        return instance;
    }

    public void debug(String mensagem) {
        registrar("DEBUG", mensagem);
    }

    public void info(String mensagem) {
        registrar("INFO", mensagem);
    }

    public void erro(String mensagem) {
        registrar("ERRO", mensagem);
    }

    private synchronized void registrar(String nivel, String mensagem) {
        String dataHora = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        String log = dataHora + " [" + nivel + "] " + mensagem;

        System.out.println(log);

        try (FileWriter arquivo = new FileWriter("app.log", true)) {
            arquivo.write(log + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Erro ao escrever no arquivo de log.");
        }
    }
}
