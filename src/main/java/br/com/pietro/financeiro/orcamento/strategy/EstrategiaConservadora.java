package br.com.pietro.financeiro.orcamento.strategy;

import br.com.pietro.financeiro.logger.AppLogger;
import org.springframework.stereotype.Component;

@Component
public class EstrategiaConservadora implements EstrategiaAlerta {

    private final AppLogger logger = AppLogger.getInstance();

    @Override
    public String getNome() {
        return "conservadora";
    }

    @Override
    public boolean deveAlertar(double percentualUsado) {
        logger.debug("Strategy conservadora avaliando o orcamento");
        return percentualUsado >= 60;
    }

    @Override
    public String gerarMensagem(double percentualUsado) {
        if (deveAlertar(percentualUsado)) {
            return "Atencao: pela estrategia conservadora, 60% do orcamento ja exige cuidado.";
        }

        return "Orcamento dentro do limite da estrategia conservadora.";
    }
}
