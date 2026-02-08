package br.gov.prefeitura.esocial.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Healthcheck publico para verificacao rapida da aplicacao.
 */
@RestController
public class HealthController {

    /**
     * Healthcheck simples exposto em /health.
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

    /**
     * Alias para compatibilidade com actuator/health mantendo a mesma resposta.
     */
    @GetMapping("/actuator/health")
    public ResponseEntity<String> actuatorHealth() {
        return health();
    }
}
