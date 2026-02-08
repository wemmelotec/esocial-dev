package br.gov.prefeitura.esocial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da aplicacao Spring Boot.
 * Executa o bootstrap do container e expoe os controllers configurados.
 */
@SpringBootApplication
public class EsocialApplication {
    /**
     * Inicializa o Spring Boot e toda a configuracao do projeto.
     *
     * @param args argumentos de linha de comando.
     */
    public static void main(String[] args) {
        SpringApplication.run(EsocialApplication.class, args);
    }
}
