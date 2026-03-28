package ru.CarDealership.domain.car;

import lombok.Builder;
import lombok.Getter;
import ru.CarDealership.domain.exceptions.DomainValidationException;

import java.math.BigDecimal;

@Builder
@Getter
public class CarFilter {
  private BigDecimal maxPrice;
  private BigDecimal minPrice;
  private String brand;
  private String modelName;
  private BodyType bodyType;
  private FuelType fuelType;
  private Integer minPower;
  private Integer maxPower;
  private Double minEngineVolume;
  private Double maxEngineVolume;
  private TransmissionType transmissionType;
  private String color;
  private DriveType driveType;

  public void validate() {
    if (minPower != null && minPower < 0) {
      throw new DomainValidationException("Min power cannot be negative");
    }
    if (maxPower != null && maxPower < 0) {
      throw new DomainValidationException("Max power cannot be negative");
    }
    if (minPrice != null && minPrice.compareTo(BigDecimal.ZERO) < 0) {
      throw new DomainValidationException("Min price cannot be negative");
    }
    if (maxPrice != null && maxPrice.compareTo(BigDecimal.ZERO) < 0) {
      throw new DomainValidationException("Max price cannot be negative");
    }
    if (minEngineVolume != null && minEngineVolume < 0) {
      throw new DomainValidationException("Min engine volume cannot be negative");
    }
    if (maxEngineVolume != null && maxEngineVolume < 0) {
      throw new DomainValidationException("Max engine volume cannot be negative");
    }
  }
}
