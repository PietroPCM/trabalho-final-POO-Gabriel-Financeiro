package br.com.pietro.financeiro.orcamento.observer;

import br.com.pietro.financeiro.orcamento.model.AnaliseOrcamento;

public interface ObservadorOrcamento {
    void atualizar(AnaliseOrcamento analise);
}
