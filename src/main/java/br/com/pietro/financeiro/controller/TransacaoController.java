package br.com.pietro.financeiro.controller;

import br.com.pietro.financeiro.entity.Transacao;
import br.com.pietro.financeiro.logger.AppLogger;
import br.com.pietro.financeiro.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    @Autowired
    private TransacaoService service;

    private final AppLogger logger = AppLogger.getInstance();

    @PostMapping
    public ResponseEntity<Transacao> criar(@RequestBody Transacao transacao) {
        logger.debug("POST /transacoes");

        Transacao criada = service.criar(transacao);

        logger.info("POST /transacoes concluido");
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @GetMapping
    public ResponseEntity<List<Transacao>> listar() {
        logger.debug("GET /transacoes");

        List<Transacao> transacoes = service.listar();

        logger.info("GET /transacoes concluido");
        return ResponseEntity.ok(transacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transacao> buscarPorId(@PathVariable Long id) {
        logger.debug("GET /transacoes/" + id);

        Optional<Transacao> transacao = service.buscarPorId(id);

        if (transacao.isEmpty()) {
            logger.erro("GET /transacoes/" + id + " - 404");
            return ResponseEntity.notFound().build();
        }

        logger.info("GET /transacoes/" + id + " - 200");
        return ResponseEntity.ok(transacao.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transacao> atualizar(
            @PathVariable Long id,
            @RequestBody Transacao transacao) {

        logger.debug("PUT /transacoes/" + id);

        Optional<Transacao> atualizada = service.atualizar(id, transacao);

        if (atualizada.isEmpty()) {
            logger.erro("PUT /transacoes/" + id + " - 404");
            return ResponseEntity.notFound().build();
        }

        logger.info("PUT /transacoes/" + id + " - 200");
        return ResponseEntity.ok(atualizada.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        logger.debug("DELETE /transacoes/" + id);

        boolean excluiu = service.excluir(id);

        if (!excluiu) {
            logger.erro("DELETE /transacoes/" + id + " - 404");
            return ResponseEntity.notFound().build();
        }

        logger.info("DELETE /transacoes/" + id + " - 204");
        return ResponseEntity.noContent().build();
    }
}
