package ru.CarDealership.infrastructure.mappers;

import org.springframework.stereotype.Component;
import ru.CarDealership.domain.car.*;
import ru.CarDealership.infrastructure.entity.*;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

@Component
public class PartEntityMapper {

    private final List<Map.Entry<Predicate<PartEntity>, Function<PartEntity, Part>>> creators;
    private final Map<String, PartType> partTypeRegistry;

    public PartEntityMapper() {
        creators = List.of(
                Map.entry(e -> e instanceof ColorPartEntity, this::createColorPart),
                Map.entry(e -> e instanceof TransmissionPartEntity, this::createTransmissionPart),
                Map.entry(e -> true, this::createPart)
        );
        Map<String, PartType> registry = new HashMap<>();
        registry.put("WHEEL", PartType.WHEEL);
        registry.put("TRANSMISSION", PartType.TRANSMISSION);
        registry.put("STEERING_WHEEL", PartType.STEERING_WHEEL);
        registry.put("INTERIOR", PartType.INTERIOR);
        registry.put("COLOR", PartType.COLOR);
        this.partTypeRegistry = registry;
    }

    public Part toDomain(PartEntity entity) {
        return creators.stream()
                .filter(e -> e.getKey().test(entity))
                .map(e -> e.getValue().apply(entity))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No creator for " + entity.getClass()));
    }

    private Part createColorPart(PartEntity entity) {
        return new ColorPart(entity.getId(), findPartType(entity.getType()), entity.getPrice(),
                extractCompatibleModelIds(entity), entity.getColor());
    }

    private Part createTransmissionPart(PartEntity entity) {
        return new TransmissionPart(entity.getId(), findPartType(entity.getType()), entity.getPrice(),
                extractCompatibleModelIds(entity), entity.getTransmissionType());
    }

    private Part createPart(PartEntity entity) {
        return new Part(entity.getId(), findPartType(entity.getType()), entity.getPrice(),
                extractCompatibleModelIds(entity));
    }

    private List<UUID> extractCompatibleModelIds(PartEntity entity) {
        return entity.getCompatibleModels().stream().map(BaseEntity::getId).toList();
    }

    private PartType findPartType(String typeName) {
        return Optional.ofNullable(partTypeRegistry.get(typeName))
                .orElseThrow(() -> new IllegalArgumentException("Unknown part type: " + typeName));
    }

    public PartEntity toEntity(Part part) {
        PartEntity entity;
        if (part instanceof ColorPart colorPart) {
            entity = new ColorPartEntity();
            entity.setColor(colorPart.getColor());
        } else if (part instanceof TransmissionPart transmissionPart) {
            entity = new TransmissionPartEntity();
            entity.setTransmissionType(transmissionPart.getTransmissionType());
        } else {
            entity = new PartEntity();
        }
        entity.setId(part.getId());
        entity.setType(part.getType().getName());
        entity.setPrice(part.getPrice());
        return entity;
    }
}
