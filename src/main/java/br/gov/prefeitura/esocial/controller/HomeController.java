package br.gov.prefeitura.esocial.controller;

import br.gov.prefeitura.esocial.service.MensagemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final MensagemService mensagemService;

    public HomeController(MensagemService mensagemService) {
        this.mensagemService = mensagemService;
    }

    @GetMapping("/")
    public String home(Model model) {
        // mensagem estática de boas-vindas (home não consome o banco)
        model.addAttribute("mensagem", "Seja bem vindo ao sistema de relatório do e-social da prefeitura de João Pessoa");
        return "home";
    }

    @GetMapping("/relatorio")
    public String relatorio(@RequestParam(value = "executar", required = false) Boolean executar,
                            Model model) {
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
