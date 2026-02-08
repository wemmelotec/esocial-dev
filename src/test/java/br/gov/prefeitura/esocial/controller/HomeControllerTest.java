package br.gov.prefeitura.esocial.controller;

import br.gov.prefeitura.esocial.service.MensagemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testa a pagina inicial garantindo modelo e view esperados.
 */
@WebMvcTest(HomeController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(HomeControllerTest.StubConfig.class)
@ImportAutoConfiguration(exclude = OAuth2ClientAutoConfiguration.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    static final String MENSAGEM_ESPERADA = "Seja bem vindo ao sistema de relatorio do e-social da prefeitura de Joao Pessoa";

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
    @WithMockUser
    void deveRetornarHomeComMensagem() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("mensagem"))
                .andExpect(model().attribute("mensagem", MENSAGEM_ESPERADA));
    }
}
