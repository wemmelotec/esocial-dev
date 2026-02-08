package br.gov.prefeitura.esocial.repository;

import br.gov.prefeitura.esocial.domain.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para a entidade Mensagem.
 */
public interface MensagemRepository extends JpaRepository<Mensagem, Long> {
}
