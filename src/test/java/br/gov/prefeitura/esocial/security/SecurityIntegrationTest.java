package br.gov.prefeitura.esocial.security;

import br.gov.prefeitura.esocial.config.GovbrAuthenticationSuccessHandler;
import br.gov.prefeitura.esocial.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testes de integracao de seguranca:
 * - garante redirecionamento de anonimos;
 * - garante upsert do usuario na autenticao OIDC;
 * - garante que logout invalida a sessao.
 */
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:esocial;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.sql.init.mode=never",
        "spring.security.oauth2.client.provider.govbr.authorization-uri=http://localhost:9999/oauth2/authorize",
        "spring.security.oauth2.client.provider.govbr.token-uri=http://localhost:9999/oauth2/token",
        "spring.security.oauth2.client.provider.govbr.jwk-set-uri=http://localhost:9999/oauth2/jwks",
        "spring.security.oauth2.client.provider.govbr.user-info-uri=http://localhost:9999/userinfo",
        "spring.security.oauth2.client.provider.govbr.end-session-uri=http://localhost:9999/logout",
        "spring.security.oauth2.client.registration.govbr.client-id=test-client",
        "spring.security.oauth2.client.registration.govbr.client-secret=test-secret",
        "spring.security.oauth2.client.registration.govbr.redirect-uri={baseUrl}/login/oauth2/code/govbr",
        "spring.security.oauth2.client.registration.govbr.scope=openid,profile,email"
})
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private GovbrAuthenticationSuccessHandler successHandler;

    @BeforeEach
    void setup() {
        userAccountRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve redirecionar anonimo para /login ao acessar rota protegida")
    void protectedRouteRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/relatorio"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("Deve criar ou atualizar usuario ao completar login OIDC")
    void shouldUpsertUserOnOidcLogin() throws Exception {
        simulateLoginSuccess("sub-123", "Cidadao Silva", "cidadao@gov.br", "12345678900");

        var usuario = userAccountRepository.findByExternalProviderAndExternalSubject("GOVBR", "sub-123").orElseThrow();
        assertThat(usuario.getNome()).isEqualTo("Cidadao Silva");
        assertThat(usuario.getEmail()).isEqualTo("cidadao@gov.br");
        assertThat(usuario.getCpf()).isEqualTo("12345678900");

        simulateLoginSuccess("sub-123", "Cidadao Atualizado", "novo@gov.br", "99988877766");
        var atualizado = userAccountRepository.findByExternalProviderAndExternalSubject("GOVBR", "sub-123").orElseThrow();
        assertThat(atualizado.getNome()).isEqualTo("Cidadao Atualizado");
        assertThat(atualizado.getEmail()).isEqualTo("novo@gov.br");
        assertThat(atualizado.getCpf()).isEqualTo("99988877766");
    }

    @Test
    @DisplayName("Logout invalida sessao e volta a exigir autenticacao")
    void logoutInvalidatesSession() throws Exception {
        var loginResult = mockMvc.perform(get("/").with(oauth2Login().oauth2User(oidcUser("logout-sub", "Logout Test", "logout@gov.br", null))))
                .andReturn();

        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);

        mockMvc.perform(get("/relatorio").session(session))
                .andExpect(status().isOk());

        mockMvc.perform(get("/logout").session(session))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/relatorio").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    private ClientRegistration govbrClient() {
        return clientRegistrationRepository.findByRegistrationId("govbr");
    }

    private void simulateLoginSuccess(String subject, String name, String email, String cpf) throws Exception {
        OidcUser oidcUser = oidcUser(subject, name, email, cpf);
        var authentication = new OAuth2AuthenticationToken(oidcUser, oidcUser.getAuthorities(), govbrClient().getRegistrationId());
        successHandler.onAuthenticationSuccess(new MockHttpServletRequest(), new MockHttpServletResponse(), authentication);
    }

    private OidcUser oidcUser(String subject, String name, String email, String cpf) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", subject);
        if (name != null) {
            claims.put("name", name);
        }
        if (email != null) {
            claims.put("email", email);
        }
        if (cpf != null) {
            claims.put("cpf", cpf);
        }

        Instant now = Instant.now();
        OidcIdToken idToken = new OidcIdToken("token-" + subject, now, now.plusSeconds(3600), claims);
        OidcUserInfo userInfo = new OidcUserInfo(claims);
        return new DefaultOidcUser(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), idToken, userInfo);
    }
}
