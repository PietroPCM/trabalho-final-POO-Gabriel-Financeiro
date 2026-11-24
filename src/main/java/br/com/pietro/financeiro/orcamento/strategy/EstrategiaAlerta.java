package br.com.pietro.financeiro.orcamento.strategy;

public interface EstrategiaAlerta {
    String getNome();
    boolean deveAlertar(double percentualUsado);
    String gerarMensagem(double percentualUsado);
}
