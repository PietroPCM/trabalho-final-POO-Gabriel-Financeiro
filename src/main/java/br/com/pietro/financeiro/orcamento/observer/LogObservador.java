package br.com.pietro.financeiro.orcamento.observer;

import br.com.pietro.financeiro.logger.AppLogger;
import br.com.pietro.financeiro.orcamento.model.AnaliseOrcamento;
import org.springframework.stereotype.Component;

@Component
public class LogObservador implements ObservadorOrcamento {

    private final AppLogger logger = AppLogger.getInstance();

    @Override
    public void atualizar(AnaliseOrcamento analise) {
        logger.info("Observer recebeu alerta de orcamento: " + analise.getMensagem());
    }
}
