package ru.CarDealership.domain.car;

import ru.CarDealership.domain.exceptions.DomainValidationException;
import ru.CarDealership.domain.exceptions.IncompatibleComponentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CarConfigurationTest {

  private CarModel modelBMW;
  private Part wheelPart;
  private TransmissionPart transmissionPart;
  private Part steeringWheelPart;
  private Part interiorPart;
  private ColorPart colorPart;

  @BeforeEach
  void setUp() {
    modelBMW = new CarModel();
    modelBMW.setId(UUID.randomUUID());
    modelBMW.setBrand("BMW");
    modelBMW.setModelName("320i");
    modelBMW.setPrice(new Price(new BigDecimal("3000000")));

    List<UUID> compatibleIds = List.of(modelBMW.getId());

    wheelPart = new Part(UUID.randomUUID(), PartType.WHEEL, new BigDecimal("50000"), compatibleIds);
    transmissionPart =
        new TransmissionPart(
            UUID.randomUUID(),
            PartType.TRANSMISSION,
            new BigDecimal("100000"),
            compatibleIds,
            TransmissionType.AUTOMATIC);
    steeringWheelPart =
        new Part(
            UUID.randomUUID(), PartType.STEERING_WHEEL, new BigDecimal("20000"), compatibleIds);
    interiorPart =
        new Part(UUID.randomUUID(), PartType.INTERIOR, new BigDecimal("150000"), compatibleIds);
    colorPart =
        new ColorPart(
            UUID.randomUUID(), PartType.COLOR, new BigDecimal("0"), compatibleIds, "Black");
  }

  @Test
  void testValidConfiguration() {
    CarConfiguration config =
        new CarConfiguration.Builder(modelBMW)
            .addPart(wheelPart)
            .addPart(transmissionPart)
            .addPart(steeringWheelPart)
            .addPart(interiorPart)
            .addPart(colorPart)
            .build();

    assertNotNull(config);
    assertEquals(modelBMW, config.getModel());
    assertEquals(5, config.getParts().size());

    assertEquals(new BigDecimal("3320000"), config.getPrice());
  }

  @Test
  void testMissingRequiredPart() {
    assertThrows(
        DomainValidationException.class,
        () -> {
          new CarConfiguration.Builder(modelBMW)
              .addPart(wheelPart)
              .addPart(transmissionPart)
              .addPart(steeringWheelPart)
              .addPart(interiorPart)
              .build();
        });
  }

  @Test
  void testIncompatiblePart() {
    CarModel modelAudi = new CarModel();
    modelAudi.setId(UUID.randomUUID());

    Part audiWheel =
        new Part(UUID.randomUUID(), PartType.WHEEL, BigDecimal.ZERO, List.of(modelAudi.getId()));

    assertThrows(
        IncompatibleComponentException.class,
        () -> {
          new CarConfiguration.Builder(modelBMW).addPart(audiWheel);
        });
  }
}
