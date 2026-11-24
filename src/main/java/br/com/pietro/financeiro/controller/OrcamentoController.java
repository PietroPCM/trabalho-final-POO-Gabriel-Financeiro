package br.com.pietro.financeiro.controller;

import br.com.pietro.financeiro.orcamento.model.AnaliseOrcamento;
import br.com.pietro.financeiro.service.OrcamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orcamento")
public class OrcamentoController {

    @Autowired
    private OrcamentoService service;

    @GetMapping("/analise")
    public ResponseEntity<?> analisar(
            @RequestParam double limite,
            @RequestParam(defaultValue = "padrao") String estrategia) {

        try {
            AnaliseOrcamento analise = service.analisar(limite, estrategia);
            return ResponseEntity.ok(analise);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/alertas")
    public ResponseEntity<List<String>> listarAlertas() {
        return ResponseEntity.ok(service.listarAlertas());
    }
}
