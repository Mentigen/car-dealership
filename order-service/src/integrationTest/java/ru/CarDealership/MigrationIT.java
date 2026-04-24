package ru.CarDealership;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;
import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThatNoException;

class MigrationIT extends BaseIntegrationTest {

    @Autowired
    DataSource dataSource;

    @Test
    void liquibaseMigrationsApplySuccessfully() {
        assertThatNoException().isThrownBy(() -> {
            try (Connection connection = dataSource.getConnection()) {
                var rs = connection.getMetaData().getTables(null, null, "orders", null);
                assert rs.next() : "Table orders should exist";
                rs = connection.getMetaData().getTables(null, null, "outbox_events", null);
                assert rs.next() : "Table outbox_events should exist";
            }
        });
    }
}
