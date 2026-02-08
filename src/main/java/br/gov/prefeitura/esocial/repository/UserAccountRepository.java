package br.gov.prefeitura.esocial.repository;

import br.gov.prefeitura.esocial.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {
    /**
     * Localiza um usuario federado a partir do provider (ex. GOVBR) e do subject OIDC.
     */
    Optional<UserAccount> findByExternalProviderAndExternalSubject(String externalProvider, String externalSubject);
}
