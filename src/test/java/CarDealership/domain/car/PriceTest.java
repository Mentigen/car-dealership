package CarDealership.domain.car;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PriceTest {

  @Test
  void testValidPrice() {
    Price price = new Price(new BigDecimal("100.50"));
    assertEquals(new BigDecimal("100.50"), price.getValue());
  }

  @Test
  void testZeroPrice() {
    Price price = new Price(BigDecimal.ZERO);
    assertEquals(BigDecimal.ZERO, price.getValue());
  }

  @Test
  void testNegativePrice() {
    assertThrows(IllegalArgumentException.class, () -> new Price(new BigDecimal("-1.00")));
  }

  @Test
  void testNullPrice() {
    assertThrows(IllegalArgumentException.class, () -> new Price(null));
  }
}
