package br.com.pietro.financeiro.repository;

import br.com.pietro.financeiro.entity.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
}
