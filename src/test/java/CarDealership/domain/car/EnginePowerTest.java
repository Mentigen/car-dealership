package CarDealership.domain.car;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnginePowerTest {

  @Test
  void testValidPower() {
    EnginePower power = new EnginePower(150);
    assertEquals(150, power.getValue());
  }

  @Test
  void testZeroPower() {
    assertThrows(IllegalArgumentException.class, () -> new EnginePower(0));
  }

  @Test
  void testNegativePower() {
    assertThrows(IllegalArgumentException.class, () -> new EnginePower(-50));
  }
}
