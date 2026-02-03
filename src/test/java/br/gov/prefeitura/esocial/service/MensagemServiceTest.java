package br.gov.prefeitura.esocial.service;

import br.gov.prefeitura.esocial.domain.Mensagem;
import br.gov.prefeitura.esocial.repository.MensagemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MensagemServiceTest {

    @Mock
    private MensagemRepository repository;

    @InjectMocks
    private MensagemService service;

    @Test
    @DisplayName("Deve retornar o texto da primeira mensagem quando existir")
    void deveRetornarPrimeiraMensagem() {
        // Given
        var msg = new Mensagem();
        msg.setTexto("Mensagem 1");
        when(repository.findAll()).thenReturn(List.of(msg));

        // When
        var resultado = service.buscarPrimeiraMensagemOuDefault();

        // Then
        assertThat(resultado).isEqualTo("Mensagem 1");
    }

    @Test
    @DisplayName("Deve retornar mensagem padrão quando não houver registros")
    void deveRetornarMensagemPadraoQuandoVazio() {
        // Given
        when(repository.findAll()).thenReturn(Collections.emptyList());

        // When
        var resultado = service.buscarPrimeiraMensagemOuDefault();

        // Then
        assertThat(resultado).isEqualTo("Nenhuma mensagem encontrada no banco");
    }
}
