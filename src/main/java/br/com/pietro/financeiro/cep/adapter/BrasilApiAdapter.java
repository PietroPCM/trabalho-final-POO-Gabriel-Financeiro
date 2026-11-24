package br.com.pietro.financeiro.cep.adapter;

import br.com.pietro.financeiro.cep.ConsultaCep;
import br.com.pietro.financeiro.cep.model.Endereco;
import br.com.pietro.financeiro.cep.provedor.BrasilApiSimulado;
import br.com.pietro.financeiro.logger.AppLogger;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

public class BrasilApiAdapter implements ConsultaCep {

    private final BrasilApiSimulado provedor;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AppLogger logger = AppLogger.getInstance();

    public BrasilApiAdapter(BrasilApiSimulado provedor) {
        this.provedor = provedor;
    }

    @Override
    public Endereco buscar(String cep) {
        logger.debug("BrasilApiAdapter traduzindo resposta do provedor");

        try {
            String json = provedor.consultar(cep);
            JsonNode dados = objectMapper.readTree(json);

            Endereco endereco = new Endereco(
                    dados.get("cep").asText(),
                    dados.get("street").asText(),
                    dados.get("neighborhood").asText(),
                    dados.get("city").asText(),
                    dados.get("state").asText()
            );

            logger.info("BrasilApiAdapter concluiu a conversao para Endereco");
            return endereco;
        } catch (Exception e) {
            logger.erro("Erro ao adaptar resposta da BrasilAPI: " + e.getMessage());
            throw new RuntimeException("Erro ao consultar CEP");
        }
    }
}
