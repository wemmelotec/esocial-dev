package br.gov.prefeitura.esocial.service;

import br.gov.prefeitura.esocial.domain.Mensagem;
import br.gov.prefeitura.esocial.repository.MensagemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
/**
 * Orquestra regras simples de leitura da entidade Mensagem.
 */
public class MensagemService {

    private final MensagemRepository repository;

    /**
     * Serviço para operações com a entidade Mensagem.
     */
    public MensagemService(MensagemRepository repository) {
        this.repository = repository;
    }

    /**
     * Retorna a primeira mensagem cadastrada ou uma string default quando não houver registros.
     */
    public String buscarPrimeiraMensagemOuDefault() {
        var mensagens = repository.findAll();
        return mensagens.isEmpty()
                ? "Nenhuma mensagem encontrada no banco"
                : mensagens.get(0).getTexto();
    }

    /**
     * Lista todas as mensagens existentes.
     */
    public List<Mensagem> listarMensagens() {
        return repository.findAll();
    }
}
