package ru.CarDealership.domain.car;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Part {
  private UUID id;
  private PartType type;
  private BigDecimal price;
  private List<UUID> compatibleModelIds;

  public boolean isCompatibleWith(CarModel model) {
    return compatibleModelIds.contains(model.getId());
  }
}
