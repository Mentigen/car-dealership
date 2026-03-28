package ru.CarDealership;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MigrationIT extends BaseIntegrationTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void allTablesCreated() throws Exception {
        Set<String> tables = new HashSet<>();
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rs = meta.getTables(null, "public", "%", new String[]{"TABLE"});
            while (rs.next()) {
                tables.add(rs.getString("TABLE_NAME"));
            }
        }

        assertTrue(tables.contains("users"), "Table 'users' should exist");
        assertTrue(tables.contains("car_models"), "Table 'car_models' should exist");
        assertTrue(tables.contains("parts"), "Table 'parts' should exist");
        assertTrue(tables.contains("part_compatible_models"), "Table 'part_compatible_models' should exist");
        assertTrue(tables.contains("car_configurations"), "Table 'car_configurations' should exist");
        assertTrue(tables.contains("car_configuration_parts"), "Table 'car_configuration_parts' should exist");
        assertTrue(tables.contains("cars"), "Table 'cars' should exist");
        assertTrue(tables.contains("orders"), "Table 'orders' should exist");
        assertTrue(tables.contains("test_drive_requests"), "Table 'test_drive_requests' should exist");
    }

    @Test
    void seedDataLoaded() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM users");
            rs.next();
            assertTrue(rs.getInt(1) >= 4, "Should have at least 4 seed users");

            rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM car_models");
            rs.next();
            assertTrue(rs.getInt(1) >= 3, "Should have at least 3 seed car models");

            rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM parts");
            rs.next();
            assertTrue(rs.getInt(1) >= 8, "Should have at least 8 seed parts");

            rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM cars");
            rs.next();
            assertTrue(rs.getInt(1) >= 1, "Should have at least 1 seed car");
        }
    }
}
