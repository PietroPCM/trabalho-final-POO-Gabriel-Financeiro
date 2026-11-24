package br.com.pietro.financeiro.orcamento.observer;

import br.com.pietro.financeiro.orcamento.model.AnaliseOrcamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificadorOrcamento {

    @Autowired
    private List<ObservadorOrcamento> observadores;

    public void notificar(AnaliseOrcamento analise) {
        for (ObservadorOrcamento observador : observadores) {
            observador.atualizar(analise);
        }
    }
}
