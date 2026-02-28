package CarDealership.domain.car;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
public class TransmissionPart extends Part {
  public TransmissionPart(
      UUID id,
      PartType type,
      BigDecimal price,
      List<UUID> compatibleModelIds,
      TransmissionType transmissionType) {
    super(id, type, price, compatibleModelIds);
    this.transmissionType = transmissionType;
  }

  private TransmissionType transmissionType;
}
