package br.gov.prefeitura.esocial.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Exibe a tela de login customizada do fluxo OAuth2.
 */
@Controller
public class AuthController {

    /**
     * Exibe a pagina de login customizada.
     * Se o usuario ja estiver autenticado, redireciona para a home evitando relogin.
     */
    @GetMapping("/login")
    public String login(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/";
        }
        return "login";
    }
}
