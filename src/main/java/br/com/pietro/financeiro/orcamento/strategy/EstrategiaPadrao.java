package br.com.pietro.financeiro.orcamento.strategy;

import br.com.pietro.financeiro.logger.AppLogger;
import org.springframework.stereotype.Component;

@Component
public class EstrategiaPadrao implements EstrategiaAlerta {

    private final AppLogger logger = AppLogger.getInstance();

    @Override
    public String getNome() {
        return "padrao";
    }

    @Override
    public boolean deveAlertar(double percentualUsado) {
        logger.debug("Strategy padrao avaliando o orcamento");
        return percentualUsado >= 80;
    }

    @Override
    public String gerarMensagem(double percentualUsado) {
        if (deveAlertar(percentualUsado)) {
            return "Atencao: voce ja utilizou 80% ou mais do seu orcamento.";
        }

        return "Orcamento dentro do limite esperado.";
    }
}
