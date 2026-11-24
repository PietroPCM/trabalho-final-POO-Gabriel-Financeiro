package br.com.pietro.financeiro.service;

import br.com.pietro.financeiro.entity.Transacao;
import br.com.pietro.financeiro.logger.AppLogger;
import br.com.pietro.financeiro.orcamento.model.AnaliseOrcamento;
import br.com.pietro.financeiro.orcamento.observer.HistoricoObservador;
import br.com.pietro.financeiro.orcamento.observer.NotificadorOrcamento;
import br.com.pietro.financeiro.orcamento.strategy.EstrategiaAlerta;
import br.com.pietro.financeiro.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrcamentoService {

    @Autowired
    private TransacaoRepository repository;

    @Autowired
    private List<EstrategiaAlerta> estrategias;

    @Autowired
    private NotificadorOrcamento notificador;

    @Autowired
    private HistoricoObservador historicoObservador;

    private final AppLogger logger = AppLogger.getInstance();

    public AnaliseOrcamento analisar(double limite, String nomeEstrategia) {
        if (limite <= 0) {
            throw new IllegalArgumentException("O limite precisa ser maior que zero");
        }

        logger.debug("Iniciando analise de orcamento com estrategia: " + nomeEstrategia);

        double totalDespesas = calcularTotalDespesas();
        double percentualUsado = (totalDespesas / limite) * 100;
        EstrategiaAlerta estrategia = buscarEstrategia(nomeEstrategia);
        boolean alerta = estrategia.deveAlertar(percentualUsado);

        AnaliseOrcamento analise = new AnaliseOrcamento(
                limite,
                totalDespesas,
                percentualUsado,
                estrategia.getNome(),
                alerta ? "ALERTA" : "OK",
                estrategia.gerarMensagem(percentualUsado)
        );

        if (alerta) {
            logger.info("Orcamento entrou em estado de alerta");
            notificador.notificar(analise);
        } else {
            logger.info("Orcamento analisado sem alerta");
        }

        return analise;
    }

    public List<String> listarAlertas() {
        return historicoObservador.listarAlertas();
    }

    private double calcularTotalDespesas() {
        double total = 0;

        for (Transacao transacao : repository.findAll()) {
            if (transacao.getTipo() != null
                    && transacao.getTipo().equalsIgnoreCase("DESPESA")
                    && transacao.getValor() != null) {
                total += transacao.getValor();
            }
        }

        return total;
    }

    private EstrategiaAlerta buscarEstrategia(String nome) {
        for (EstrategiaAlerta estrategia : estrategias) {
            if (estrategia.getNome().equalsIgnoreCase(nome)) {
                return estrategia;
            }
        }

        throw new IllegalArgumentException("Estrategia invalida. Use padrao ou conservadora");
    }
}
