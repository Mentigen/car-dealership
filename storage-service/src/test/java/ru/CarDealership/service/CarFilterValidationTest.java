package ru.CarDealership.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.CarDealership.api.dto.CarFilterRequest;
import ru.CarDealership.domain.exceptions.DomainValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarFilterValidationTest {

    @Test
    void testNegativePowerThrowsException() {
        CarFilterRequest filterRequest = new CarFilterRequest();
        filterRequest.setMinPower(-100);
        Pageable pageable = PageRequest.of(0, 10);

        CarService service = new CarService(null, null, null);
        assertThrows(DomainValidationException.class, () -> service.searchCars(filterRequest, pageable));
    }

    @Test
    void testNegativePriceThrowsException() {
        CarFilterRequest filterRequest = new CarFilterRequest();
        filterRequest.setMinPrice(java.math.BigDecimal.valueOf(-500));
        Pageable pageable = PageRequest.of(0, 10);

        CarService service = new CarService(null, null, null);
        assertThrows(DomainValidationException.class, () -> service.searchCars(filterRequest, pageable));
    }
}
