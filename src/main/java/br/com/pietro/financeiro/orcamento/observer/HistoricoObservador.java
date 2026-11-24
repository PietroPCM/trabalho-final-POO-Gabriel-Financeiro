package br.com.pietro.financeiro.orcamento.observer;

import br.com.pietro.financeiro.orcamento.model.AnaliseOrcamento;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HistoricoObservador implements ObservadorOrcamento {

    private final List<String> alertas = new ArrayList<>();

    @Override
    public void atualizar(AnaliseOrcamento analise) {
        alertas.add(analise.getMensagem());
    }

    public List<String> listarAlertas() {
        return alertas;
    }
}
