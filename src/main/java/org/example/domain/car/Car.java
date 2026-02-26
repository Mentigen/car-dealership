package org.example.domain.car;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(of = "id")
@Getter @Setter @AllArgsConstructor
public class Car {
    private UUID id;
    private CarConfiguration carConfiguration;

    public BigDecimal getPrice() {
        return carConfiguration.getPrice();
    }
}
