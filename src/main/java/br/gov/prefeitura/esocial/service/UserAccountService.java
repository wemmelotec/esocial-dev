package br.gov.prefeitura.esocial.service;

import br.gov.prefeitura.esocial.domain.UserAccount;
import br.gov.prefeitura.esocial.repository.UserAccountRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servico de negocio para persistir usuarios autenticados via gov.br.
 */
@Service
public class UserAccountService {

    private static final String PROVIDER_GOVBR = "GOVBR";
    private static final String CLAIM_CPF = "cpf";

    private final UserAccountRepository repository;

    /**
     * Serviço responsável por manter o usuário federado no banco local.
     */
    public UserAccountService(UserAccountRepository repository) {
        this.repository = repository;
    }

    /**
     * Cria ou atualiza um usuario local a partir de um OidcUser autenticado.
     * Usa provider + subject como chave e atualiza nome/email/cpf.
     */
    @Transactional
    public UserAccount upsertFromOidcUser(OidcUser oidcUser) {
        String subject = oidcUser.getSubject();
        UserAccount account = repository.findByExternalProviderAndExternalSubject(PROVIDER_GOVBR, subject)
                .orElseGet(() -> newAccount(subject));

        account.setNome(resolveNome(oidcUser));
        account.setEmail(oidcUser.getEmail());
        account.setCpf(oidcUser.getClaimAsString(CLAIM_CPF));

        return repository.save(account);
    }

    private UserAccount newAccount(String subject) {
        UserAccount account = new UserAccount();
        account.setExternalProvider(PROVIDER_GOVBR);
        account.setExternalSubject(subject);
        return account;
    }

    /**
     * Tenta extrair um nome amigavel do OIDC; usa preferredUsername ou name como fallback.
     */
    private String resolveNome(OidcUser oidcUser) {
        if (oidcUser.getFullName() != null) {
            return oidcUser.getFullName();
        }
        if (oidcUser.getPreferredUsername() != null) {
            return oidcUser.getPreferredUsername();
        }
        return oidcUser.getName();
    }
}
