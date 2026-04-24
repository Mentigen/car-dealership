package ru.CarDealership;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class MigrationIT extends BaseIntegrationTest {

    @Test
    void liquibaseMigrationsApplySuccessfully() {
        assertThatCode(() -> Class.forName("org.postgresql.Driver")).doesNotThrowAnyException();
    }
}
