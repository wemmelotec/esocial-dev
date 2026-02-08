package br.gov.prefeitura.esocial.config;

import br.gov.prefeitura.esocial.service.UserAccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Handler de sucesso para o login OIDC do gov.br.
 */
@Component
public class GovbrAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserAccountService userAccountService;

    /**
     * Handler chamado apos um login OAuth2/OIDC bem-sucedido.
     * Persiste ou atualiza o usuario local e redireciona para a home.
     */
    public GovbrAuthenticationSuccessHandler(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
        setDefaultTargetUrl("/");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        if (authentication.getPrincipal() instanceof OidcUser oidcUser) {
            userAccountService.upsertFromOidcUser(oidcUser);
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
