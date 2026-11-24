package br.com.pietro.financeiro.cep.adapter;

import br.com.pietro.financeiro.cep.ConsultaCep;
import br.com.pietro.financeiro.cep.model.Endereco;
import br.com.pietro.financeiro.cep.provedor.ViaCepSimulado;
import br.com.pietro.financeiro.logger.AppLogger;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

public class ViaCepAdapter implements ConsultaCep {

    private final ViaCepSimulado provedor;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AppLogger logger = AppLogger.getInstance();

    public ViaCepAdapter(ViaCepSimulado provedor) {
        this.provedor = provedor;
    }

    @Override
    public Endereco buscar(String cep) {
        logger.debug("ViaCepAdapter traduzindo resposta do provedor");

        try {
            String json = provedor.consultar(cep);
            JsonNode dados = objectMapper.readTree(json);

            Endereco endereco = new Endereco(
                    dados.get("cep").asText(),
                    dados.get("logradouro").asText(),
                    dados.get("bairro").asText(),
                    dados.get("localidade").asText(),
                    dados.get("uf").asText()
            );

            logger.info("ViaCepAdapter concluiu a conversao para Endereco");
            return endereco;
        } catch (Exception e) {
            logger.erro("Erro ao adaptar resposta do ViaCEP: " + e.getMessage());
            throw new RuntimeException("Erro ao consultar CEP");
        }
    }
}
