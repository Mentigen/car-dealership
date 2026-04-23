package ru.CarDealership.infrastructure.specifications;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import ru.CarDealership.domain.car.*;
import ru.CarDealership.infrastructure.entity.*;

import java.math.BigDecimal;

public class CarEntitySpecifications {

    public static Specification<CarEntity> hasBrand(String brand) {
        return (root, query, cb) -> {
            if (brand == null || brand.isBlank()) return cb.conjunction();
            Join<CarEntity, CarConfigurationEntity> config = root.join("carConfiguration");
            Join<CarConfigurationEntity, CarModelEntity> model = config.join("carModel");
            return cb.equal(cb.lower(model.get("brand")), brand.toLowerCase());
        };
    }

    public static Specification<CarEntity> hasModel(String modelName) {
        return (root, query, cb) -> {
            if (modelName == null || modelName.isBlank()) return cb.conjunction();
            Join<CarEntity, CarConfigurationEntity> config = root.join("carConfiguration");
            Join<CarConfigurationEntity, CarModelEntity> model = config.join("carModel");
            return cb.equal(cb.lower(model.get("modelName")), modelName.toLowerCase());
        };
    }

    public static Specification<CarEntity> hasBodyType(BodyType bodyType) {
        return (root, query, cb) -> {
            if (bodyType == null) return cb.conjunction();
            Join<CarEntity, CarConfigurationEntity> config = root.join("carConfiguration");
            Join<CarConfigurationEntity, CarModelEntity> model = config.join("carModel");
            return cb.equal(model.get("body"), bodyType);
        };
    }

    public static Specification<CarEntity> hasFuelType(FuelType fuelType) {
        return (root, query, cb) -> {
            if (fuelType == null) return cb.conjunction();
            Join<CarEntity, CarConfigurationEntity> config = root.join("carConfiguration");
            Join<CarConfigurationEntity, CarModelEntity> model = config.join("carModel");
            return cb.equal(model.get("fuelType"), fuelType);
        };
    }

    public static Specification<CarEntity> hasDriveType(DriveType driveType) {
        return (root, query, cb) -> {
            if (driveType == null) return cb.conjunction();
            Join<CarEntity, CarConfigurationEntity> config = root.join("carConfiguration");
            Join<CarConfigurationEntity, CarModelEntity> model = config.join("carModel");
            return cb.equal(model.get("drive"), driveType);
        };
    }

    public static Specification<CarEntity> hasPowerBetween(Integer minPower, Integer maxPower) {
        return (root, query, cb) -> {
            Join<CarEntity, CarConfigurationEntity> config = root.join("carConfiguration");
            Join<CarConfigurationEntity, CarModelEntity> model = config.join("carModel");
            Predicate predicate = cb.conjunction();
            if (minPower != null) predicate = cb.and(predicate, cb.greaterThanOrEqualTo(model.get("enginePower"), minPower));
            if (maxPower != null) predicate = cb.and(predicate, cb.lessThanOrEqualTo(model.get("enginePower"), maxPower));
            return predicate;
        };
    }

    public static Specification<CarEntity> hasEngineVolumeBetween(Double minVolume, Double maxVolume) {
        return (root, query, cb) -> {
            Join<CarEntity, CarConfigurationEntity> config = root.join("carConfiguration");
            Join<CarConfigurationEntity, CarModelEntity> model = config.join("carModel");
            Predicate predicate = cb.conjunction();
            if (minVolume != null) predicate = cb.and(predicate, cb.greaterThanOrEqualTo(model.get("engineVolume"), minVolume));
            if (maxVolume != null) predicate = cb.and(predicate, cb.lessThanOrEqualTo(model.get("engineVolume"), maxVolume));
            return predicate;
        };
    }

    public static Specification<CarEntity> hasPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, cb) -> {
            if (minPrice == null && maxPrice == null) return cb.conjunction();
            Join<CarEntity, CarConfigurationEntity> config = root.join("carConfiguration");
            Join<CarConfigurationEntity, CarModelEntity> model = config.join("carModel");
            Predicate predicate = cb.conjunction();
            if (minPrice != null) predicate = cb.and(predicate, cb.greaterThanOrEqualTo(model.get("price"), minPrice));
            if (maxPrice != null) predicate = cb.and(predicate, cb.lessThanOrEqualTo(model.get("price"), maxPrice));
            return predicate;
        };
    }

    public static Specification<CarEntity> hasColor(String color) {
        return (root, query, cb) -> {
            if (color == null || color.isBlank()) return cb.conjunction();
            Join<CarEntity, CarConfigurationEntity> config = root.join("carConfiguration");
            Join<CarConfigurationEntity, PartEntity> parts = config.join("parts");
            if (query != null) query.distinct(true);
            return cb.and(cb.isNotNull(parts.get("color")), cb.equal(cb.lower(parts.get("color")), color.toLowerCase()));
        };
    }

    public static Specification<CarEntity> hasTransmissionType(TransmissionType transmissionType) {
        return (root, query, cb) -> {
            if (transmissionType == null) return cb.conjunction();
            Join<CarEntity, CarConfigurationEntity> config = root.join("carConfiguration");
            Join<CarConfigurationEntity, PartEntity> parts = config.join("parts");
            if (query != null) query.distinct(true);
            return cb.and(cb.isNotNull(parts.get("transmissionType")), cb.equal(parts.get("transmissionType"), transmissionType));
        };
    }

    public static Specification<CarEntity> fromFilter(CarFilter filter) {
        return Specification
                .where(hasBrand(filter.getBrand()))
                .and(hasModel(filter.getModelName()))
                .and(hasBodyType(filter.getBodyType()))
                .and(hasFuelType(filter.getFuelType()))
                .and(hasDriveType(filter.getDriveType()))
                .and(hasPriceBetween(filter.getMinPrice(), filter.getMaxPrice()))
                .and(hasPowerBetween(filter.getMinPower(), filter.getMaxPower()))
                .and(hasEngineVolumeBetween(filter.getMinEngineVolume(), filter.getMaxEngineVolume()))
                .and(hasColor(filter.getColor()))
                .and(hasTransmissionType(filter.getTransmissionType()));
    }
}
