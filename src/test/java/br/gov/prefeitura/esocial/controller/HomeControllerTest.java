package br.gov.prefeitura.esocial.controller;

import br.gov.prefeitura.esocial.service.MensagemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
@Import(HomeControllerTest.StubConfig.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    static final String MENSAGEM_ESPERADA = "Seja bem vindo ao sistema de relatório do e-social da prefeitura de João Pessoa";

    static class StubConfig {
        @Bean
        MensagemService mensagemService() {
            return new MensagemService(null) {
                @Override
                public java.util.List<br.gov.prefeitura.esocial.domain.Mensagem> listarMensagens() {
                    return Collections.emptyList();
                }

                @Override
                public String buscarPrimeiraMensagemOuDefault() {
                    return MENSAGEM_ESPERADA;
                }
            };
        }
    }

    @Test
    @DisplayName("Deve retornar status 200, atributo 'mensagem' e view 'home' na raiz")
    void deveRetornarHomeComMensagem() throws Exception {
        // Given / When / Then
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("mensagem"))
                .andExpect(model().attribute("mensagem", MENSAGEM_ESPERADA));
    }
}
