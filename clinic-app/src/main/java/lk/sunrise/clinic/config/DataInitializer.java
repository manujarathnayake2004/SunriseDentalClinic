package lk.sunrise.clinic.config;

import org.springframework.context.annotation.Configuration;

/**
 * Production configuration intentionally contains no seeded/demo records.
 * The first authorized administrator is created through the one-time /setup flow.
 * Dentists, treatments and additional staff accounts must be entered by an administrator.
 */
@Configuration
public class DataInitializer {
    // No demo data is inserted automatically.
}
