package CarDealership.domain.car;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class Price {
  BigDecimal value;

  public Price(BigDecimal value) {
    if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Price cannot be negative");
    }
    this.value = value;
  }
}
