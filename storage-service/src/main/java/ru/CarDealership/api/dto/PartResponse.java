package ru.CarDealership.api.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "partKind")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ColorPartResponse.class, name = "COLOR"),
        @JsonSubTypes.Type(value = TransmissionPartResponse.class, name = "TRANSMISSION"),
        @JsonSubTypes.Type(value = BasePartResponse.class, name = "BASE")
})
public sealed interface PartResponse permits ColorPartResponse, TransmissionPartResponse, BasePartResponse {
    UUID id();
    String type();
    BigDecimal price();
    List<UUID> compatibleModelIds();
}
