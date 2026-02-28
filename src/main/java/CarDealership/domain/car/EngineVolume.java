package CarDealership.domain.car;

import lombok.Value;

@Value
public class EngineVolume {
  double value;

  public EngineVolume(double value) {
    if (value <= 0) {
      throw new IllegalArgumentException("Engine volume must be positive");
    }
    this.value = value;
  }
}
