package ru.CarDealership.service;

import ru.CarDealership.domain.car.CarFilter;
import ru.CarDealership.domain.exceptions.DomainValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CarFilterValidationTest {

  @Test
  void testNegativePowerThrowsException() {
    CarFilter filter = CarFilter.builder().minPower(-100).build();

    CarService service = new CarService(null, null, null);
    assertThrows(DomainValidationException.class, () -> service.searchCars(filter));
  }

  @Test
  void testNegativePriceThrowsException() {
    CarFilter filter = CarFilter.builder().minPrice(java.math.BigDecimal.valueOf(-500)).build();

    CarService service = new CarService(null, null, null);
    assertThrows(DomainValidationException.class, () -> service.searchCars(filter));
  }
}
