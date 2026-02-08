package br.gov.prefeitura.esocial.controller;

import br.gov.prefeitura.esocial.service.MensagemService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controla as paginas web (home e relatorio) protegidas por autenticacao.
 */
@Controller
public class HomeController {

    private final MensagemService mensagemService;

    /**
     * Controller das paginas principais (home/relatorio).
     */
    public HomeController(MensagemService mensagemService) {
        this.mensagemService = mensagemService;
    }

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal OidcUser oidcUser) {
        model.addAttribute("mensagem", "Seja bem vindo ao sistema de relatorio do e-social da prefeitura de Joao Pessoa");
        if (oidcUser != null) {
            String nome = oidcUser.getFullName();
            if (nome == null) {
                nome = oidcUser.getPreferredUsername() != null ? oidcUser.getPreferredUsername() : oidcUser.getName();
            }
            // Preenche informacoes do usuario autenticado para exibicao
            model.addAttribute("usuarioNome", nome != null ? nome : "Usuario");
            model.addAttribute("usuarioEmail", oidcUser.getEmail());
            model.addAttribute("usuarioSubject", oidcUser.getSubject());
            model.addAttribute("usuarioProvider", "gov.br");
        }
        return "home";
    }

    @GetMapping("/relatorio")
    public String relatorio(@RequestParam(value = "executar", required = false) Boolean executar,
                            Model model) {
        // Controla o fluxo da pagina: apenas executa consulta quando o usuario solicita
        if (Boolean.TRUE.equals(executar)) {
            var mensagens = mensagemService.listarMensagens();
            model.addAttribute("mensagens", mensagens);
            model.addAttribute("executado", true);
        } else {
            model.addAttribute("executado", false);
        }
        return "relatorio";
    }
}
