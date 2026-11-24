package br.com.pietro.financeiro.cep.proxy;

import br.com.pietro.financeiro.cep.ConsultaCep;
import br.com.pietro.financeiro.cep.model.Endereco;
import br.com.pietro.financeiro.logger.AppLogger;

import java.util.HashMap;
import java.util.Map;

public class ConsultaCepProxy implements ConsultaCep {

    private final ConsultaCep consultaReal;
    private final Map<String, Endereco> cache = new HashMap<>();
    private final AppLogger logger = AppLogger.getInstance();

    public ConsultaCepProxy(ConsultaCep consultaReal) {
        this.consultaReal = consultaReal;
    }

    @Override
    public Endereco buscar(String cep) {
        if (cep == null || !cep.matches("\\d{8}")) {
            logger.erro("CEP invalido bloqueado pelo Proxy: " + cep);
            throw new IllegalArgumentException("O CEP deve possuir exatamente 8 digitos");
        }

        if (cache.containsKey(cep)) {
            logger.info("CEP " + cep + " retornado do cache");
            return cache.get(cep);
        }

        logger.info("CEP " + cep + " consultado no provedor");
        Endereco endereco = consultaReal.buscar(cep);
        cache.put(cep, endereco);
        return endereco;
    }
}
