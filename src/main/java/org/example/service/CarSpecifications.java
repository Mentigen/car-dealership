package org.example.service;

import org.example.domain.car.*;

import java.math.BigDecimal;
import java.util.function.Predicate;

public class CarSpecifications {
  public static Predicate<Car> hasBrand(String brand) {
    return car -> {
      if (brand == null) {
        return true;
      } else {
        return car.getCarConfiguration().getModel().getBrand().equalsIgnoreCase(brand);
      }
    };
  }

  public static Predicate<Car> hasModel(String model) {
    return car -> {
      if (model == null) {
        return true;
      } else {
        return car.getCarConfiguration().getModel().getModelName().equalsIgnoreCase(model);
      }
    };
  }

  public static Predicate<Car> hasBodyType(BodyType bodyType) {
    return car -> {
      if (bodyType == null) {
        return true;
      } else {
        return car.getCarConfiguration().getModel().getBody() == bodyType;
      }
    };
  }

  public static Predicate<Car> hasFuelType(FuelType fuelType) {
    return car -> {
      if (fuelType == null) {
        return true;
      } else {
        return car.getCarConfiguration().getModel().getFuel() == fuelType;
      }
    };
  }

  public static Predicate<Car> hasDriveType(DriveType driveType) {
    return car -> {
      if (driveType == null) {
        return true;
      } else {
        return car.getCarConfiguration().getModel().getDrive() == driveType;
      }
    };
  }

  public static Predicate<Car> hasPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
    return car -> {
      BigDecimal price = car.getPrice();
      if (minPrice != null && price.compareTo(minPrice) < 0) return false;
      if (maxPrice != null && price.compareTo(maxPrice) > 0) return false;
      return true;
    };
  }

  public static Predicate<Car> hasPowerBetween(Integer minPower, Integer maxPower) {
    return car -> {
      int power = car.getCarConfiguration().getModel().getEnginePower();
      if (minPower != null && power < minPower) return false;
      if (maxPower != null && power > maxPower) return false;
      return true;
    };
  }

  public static Predicate<Car> hasEngineVolumeBetween(Double minVolume, Double maxVolume) {
    return car -> {
      double volume = car.getCarConfiguration().getModel().getEngineVolume();
      if (minVolume != null && volume < minVolume) return false;
      if (maxVolume != null && volume > maxVolume) return false;
      return true;
    };
  }

  public static Predicate<Car> hasColor(String color) {
    return car -> {
      if (color == null) return true;
      return car.getCarConfiguration().getParts().stream()
              .filter(part -> part instanceof ColorPart)
              .map(part -> (ColorPart) part)
              .anyMatch(colorPart -> colorPart.getColor().equalsIgnoreCase(color));
    };
  }

  public static Predicate<Car> hasTransmissionType(TransmissionType transmissionType) {
    return car -> {
      if (transmissionType == null) return true;
      return car.getCarConfiguration().getParts().stream()
              .filter(part -> part instanceof TransmissionPart)
              .map(part -> (TransmissionPart) part)
              .anyMatch(tp -> tp.getTransmissionType() == transmissionType);
    };
  }
}
