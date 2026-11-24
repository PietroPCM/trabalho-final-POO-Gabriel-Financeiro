package br.com.pietro.financeiro.cep.provedor;

public class ViaCepSimulado {

    public String consultar(String cep) {
        return """
                {
                  "cep": "%s",
                  "logradouro": "Rua Parana",
                  "bairro": "Centro",
                  "localidade": "Cascavel",
                  "uf": "PR"
                }
                """.formatted(cep);
    }
}
