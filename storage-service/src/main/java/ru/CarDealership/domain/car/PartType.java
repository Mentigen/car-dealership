package ru.CarDealership.domain.car;

import lombok.Value;

@Value
public class PartType {
    String name;

    public static PartType WHEEL = new PartType("WHEEL");
    public static PartType TRANSMISSION = new PartType("TRANSMISSION");
    public static PartType STEERING_WHEEL = new PartType("STEERING_WHEEL");
    public static PartType INTERIOR = new PartType("INTERIOR");
    public static PartType COLOR = new PartType("COLOR");
}
