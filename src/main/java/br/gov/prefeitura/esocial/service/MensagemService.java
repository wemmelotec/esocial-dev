package br.gov.prefeitura.esocial.service;

import br.gov.prefeitura.esocial.domain.Mensagem;
import br.gov.prefeitura.esocial.repository.MensagemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MensagemService {

    private final MensagemRepository repository;

    public MensagemService(MensagemRepository repository) {
        this.repository = repository;
    }

    public String buscarPrimeiraMensagemOuDefault() {
        var mensagens = repository.findAll();
        return mensagens.isEmpty()
                ? "Nenhuma mensagem encontrada no banco"
                : mensagens.get(0).getTexto();
    }

    public List<Mensagem> listarMensagens() {
        return repository.findAll();
    }
}
