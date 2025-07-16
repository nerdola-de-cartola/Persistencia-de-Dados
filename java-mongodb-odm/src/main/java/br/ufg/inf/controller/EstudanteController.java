package br.ufg.inf.controller;

import br.ufg.inf.model.Estudante;
import br.ufg.inf.repository.EstudanteRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estudantes")
public class EstudanteController {

    private final EstudanteRepository repo;

    public EstudanteController(EstudanteRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Estudante> listar() {
        return repo.findAll();
    }

    @PostMapping
    public Estudante salvar(@RequestBody Estudante e) {
        return repo.save(e);
    }
}