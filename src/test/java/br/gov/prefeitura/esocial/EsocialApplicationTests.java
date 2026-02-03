package br.gov.prefeitura.esocial;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=none",
        "spring.sql.init.mode=never"
})
class EsocialApplicationTests {

    @Test
    void contextLoads() {
        // Verifica se o contexto Spring sobe sem falhas.
    }
}
