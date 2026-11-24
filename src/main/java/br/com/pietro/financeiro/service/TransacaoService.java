package br.com.pietro.financeiro.service;

import br.com.pietro.financeiro.entity.Transacao;
import br.com.pietro.financeiro.logger.AppLogger;
import br.com.pietro.financeiro.logger.SpringAppLogger;
import br.com.pietro.financeiro.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository repository;

    // Versao criada apenas para comparar o Singleton manual com o Singleton do Spring.
    @Autowired
    private SpringAppLogger springAppLogger;

    private final AppLogger logger = AppLogger.getInstance();

    public Transacao criar(Transacao transacao) {
        logger.debug("Criando uma nova transacao");

        try {
            Transacao salva = repository.save(transacao);
            logger.info("Transacao criada com sucesso. ID: " + salva.getId());
            return salva;
        } catch (Exception e) {
            logger.erro("Erro ao criar transacao: " + e.getMessage());
            throw e;
        }
    }

    public List<Transacao> listar() {
        logger.debug("Listando todas as transacoes");
        List<Transacao> transacoes = repository.findAll();
        logger.info("Transacoes listadas com sucesso");
        return transacoes;
    }

    public Optional<Transacao> buscarPorId(Long id) {
        logger.debug("Buscando transacao de ID: " + id);
        Optional<Transacao> transacao = repository.findById(id);

        if (transacao.isEmpty()) {
            logger.erro("Transacao nao encontrada. ID: " + id);
        } else {
            logger.info("Transacao encontrada. ID: " + id);
        }

        return transacao;
    }

    public Optional<Transacao> atualizar(Long id, Transacao novosDados) {
        logger.debug("Atualizando transacao de ID: " + id);
        Optional<Transacao> encontrada = repository.findById(id);

        if (encontrada.isEmpty()) {
            logger.erro("Transacao nao encontrada. ID: " + id);
            return Optional.empty();
        }

        Transacao transacao = encontrada.get();
        transacao.setDescricao(novosDados.getDescricao());
        transacao.setValor(novosDados.getValor());
        transacao.setData(novosDados.getData());
        transacao.setTipo(novosDados.getTipo());

        Transacao atualizada = repository.save(transacao);
        logger.info("Transacao atualizada com sucesso. ID: " + id);
        return Optional.of(atualizada);
    }

    public boolean excluir(Long id) {
        logger.debug("Excluindo transacao de ID: " + id);

        if (!repository.existsById(id)) {
            logger.erro("Transacao nao encontrada. ID: " + id);
            return false;
        }

        repository.deleteById(id);
        logger.info("Transacao excluida com sucesso. ID: " + id);
        return true;
    }
}
