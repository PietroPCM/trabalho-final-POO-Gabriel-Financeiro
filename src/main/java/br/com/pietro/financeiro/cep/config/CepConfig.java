package br.com.pietro.financeiro.cep.config;

import br.com.pietro.financeiro.cep.ConsultaCep;
import br.com.pietro.financeiro.cep.adapter.BrasilApiAdapter;
import br.com.pietro.financeiro.cep.adapter.ViaCepAdapter;
import br.com.pietro.financeiro.cep.provedor.BrasilApiSimulado;
import br.com.pietro.financeiro.cep.provedor.ViaCepSimulado;
import br.com.pietro.financeiro.cep.proxy.ConsultaCepProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CepConfig {

    @Bean
    public ConsultaCep consultaCep() {
        // Provedor atual:
        ConsultaCep provedor = new ViaCepAdapter(new ViaCepSimulado());

        // Para usar a BrasilAPI, a linha acima pode ser trocada por:
        // ConsultaCep provedor = new BrasilApiAdapter(new BrasilApiSimulado());

        return new ConsultaCepProxy(provedor);
    }
}
