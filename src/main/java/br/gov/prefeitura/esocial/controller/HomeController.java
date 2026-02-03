package br.gov.prefeitura.esocial.controller;

import br.gov.prefeitura.esocial.repository.MensagemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final MensagemRepository repository;

    public HomeController(MensagemRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/relatorio")
    public String relatorio(@RequestParam(value = "executar", required = false) Boolean executar,
                            Model model) {
        if (Boolean.TRUE.equals(executar)) {
            var mensagens = repository.findAll();
            model.addAttribute("mensagens", mensagens);
            model.addAttribute("executado", true);
        } else {
            model.addAttribute("executado", false);
        }
        return "relatorio";
    }
}
