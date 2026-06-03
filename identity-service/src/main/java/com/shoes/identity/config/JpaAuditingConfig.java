package com.shoes.identity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * Bat Spring Data JPA Auditing:
 *   - @CreatedDate / @LastModifiedDate tu dong dien gia tri tu LocalDateTime.now()
 *   - @CreatedBy / @LastModifiedBy lay tu AuditorAware bean
 *
 * Hien tai AuditorAware tra ve null (chua co JWT principal). Step JWT sau:
 * doc user_id tu SecurityContext de set vao audit field.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<Long> auditorProvider() {
        // TODO: thay bang doc SecurityContext khi co JWT auth
        return () -> Optional.empty();
    }
}
