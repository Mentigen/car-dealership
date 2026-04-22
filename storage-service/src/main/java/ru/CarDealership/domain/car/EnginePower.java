package ru.CarDealership.domain.car;

import lombok.Value;

@Value
public class EnginePower {
    int value;

    public EnginePower(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Engine power must be positive");
        }
        this.value = value;
    }
}
