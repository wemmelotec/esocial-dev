package br.gov.prefeitura.esocial.domain;

import jakarta.persistence.*;

/**
 * Entidade JPA simples para mensagens exibidas nos relatorios.
 */
@Entity
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Texto livre armazenado na tabela mensagem
    private String texto;

    public Long getId() {
        return id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
