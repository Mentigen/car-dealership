package org.example.domain.car;

import lombok.Getter;
import org.example.domain.exceptions.DomainValidationException;
import org.example.domain.exceptions.IncompatibleComponentException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Long.sum;

@Getter
public class CarConfiguration {
  private CarModel model;
  private List<Part> parts;

  private static final List<PartType> REQUIRED_PART_TYPES =
      List.of(
          PartType.WHEEL,
          PartType.TRANSMISSION,
          PartType.STEERING_WHEEL,
          PartType.INTERIOR,
          PartType.COLOR);

  private CarConfiguration(CarModel model, List<Part> parts) {
    this.model = model;
    this.parts = parts;
  }

  public BigDecimal getPrice() {
    BigDecimal partsPrice =
        parts.stream().map(Part::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    return model.getPrice().add(partsPrice);
  }

  public static class Builder {
    private CarModel model;
    private List<Part> parts = new ArrayList<>();

    public Builder(CarModel model) {
      this.model = model;
    }

    public Builder addPart(Part part) {
      if (part.isCompatibleWith(model)) {
        this.parts.add(part);
      } else {
        throw new IncompatibleComponentException("The part is not compatible with this car model");
      }
      return this;
    }

    public CarConfiguration build() {
      REQUIRED_PART_TYPES.stream()
          .filter(type -> parts.stream().noneMatch(p -> p.getType().equals(type)))
          .findFirst()
          .ifPresent(
              missingType -> {
                throw new DomainValidationException("Missing required part type: " + missingType);
              });

      return new CarConfiguration(this.model, this.parts);
    }
  }
}
