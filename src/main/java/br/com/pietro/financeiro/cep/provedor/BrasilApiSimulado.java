package br.com.pietro.financeiro.cep.provedor;

public class BrasilApiSimulado {

    public String consultar(String cep) {
        return """
                {
                  "cep": "%s",
                  "street": "Rua Parana",
                  "neighborhood": "Centro",
                  "city": "Cascavel",
                  "state": "PR"
                }
                """.formatted(cep);
    }
}
