package br.gov.prefeitura.esocial.controller;

import br.gov.prefeitura.esocial.repository.MensagemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final MensagemRepository repository;

    public HomeController(MensagemRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String home(Model model) {
        var mensagens = repository.findAll();

        String texto = mensagens.isEmpty()
                ? "Nenhuma mensagem encontrada no banco"
                : mensagens.get(0).getTexto();

        model.addAttribute("mensagem", texto);

        return "home";
    }
}
