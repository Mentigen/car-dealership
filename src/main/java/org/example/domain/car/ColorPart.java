package org.example.domain.car;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
public class ColorPart extends Part {
  private final String color;

  public ColorPart(
      UUID id, PartType type, BigDecimal price, List<UUID> compatibleModelIds, String color) {
    super(id, type, price, compatibleModelIds);
    this.color = color;
  }
}
