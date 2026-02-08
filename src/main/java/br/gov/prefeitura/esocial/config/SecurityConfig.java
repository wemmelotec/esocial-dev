package br.gov.prefeitura.esocial.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Configuracao central de seguranca HTTP (rotas publicas, OAuth2 login e logout OIDC).
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final GovbrAuthenticationSuccessHandler successHandler;

    /**
     * Injeta dependencias de OAuth2 (registrations) e handler de sucesso customizado.
     */
    public SecurityConfig(ClientRegistrationRepository clientRegistrationRepository,
                          GovbrAuthenticationSuccessHandler successHandler) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.successHandler = successHandler;
    }

    /**
     * Define a cadeia de filtros de seguranca:
     * - Rotas publicas liberadas (login, assets, health);
     * - Demais rotas exigem autenticacao OAuth2;
     * - Usa pagina de login customizada e handler para persistir usuario;
     * - Configura logout invalidando sessao e opcionalmente chamando end_session do provider.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**", "/health", "/actuator/health").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .successHandler(successHandler)
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler(oidcLogoutSuccessHandler())
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers("/health"));

        return http.build();
    }

    /**
     * Configura o fluxo de logout OIDC quando o provedor oferece end_session.
     * O postLogoutRedirectUri leva o usuario de volta para a pagina de login com indicador de logout.
     */
    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        var oidc = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        oidc.setPostLogoutRedirectUri("{baseUrl}/login?logout");
        return oidc;
    }
}
