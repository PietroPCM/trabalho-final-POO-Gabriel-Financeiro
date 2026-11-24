package br.com.pietro.financeiro.cep;

import br.com.pietro.financeiro.cep.model.Endereco;

public interface ConsultaCep {
    Endereco buscar(String cep);
}
