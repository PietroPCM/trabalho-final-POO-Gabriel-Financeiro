package br.com.pietro.financeiro.controller;

import br.com.pietro.financeiro.cep.ConsultaCep;
import br.com.pietro.financeiro.cep.model.Endereco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cep")
public class CepController {

    @Autowired
    private ConsultaCep consultaCep;

    @GetMapping("/{cep}")
    public ResponseEntity<?> buscar(@PathVariable String cep) {
        try {
            Endereco endereco = consultaCep.buscar(cep);
            return ResponseEntity.ok(endereco);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
