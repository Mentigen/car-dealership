package ru.CarDealership.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.CarDealership.api.dto.BasePartResponse;
import ru.CarDealership.api.dto.ColorPartResponse;
import ru.CarDealership.api.dto.PartResponse;
import ru.CarDealership.api.dto.TransmissionPartResponse;
import ru.CarDealership.domain.car.ColorPart;
import ru.CarDealership.domain.car.Part;
import ru.CarDealership.domain.car.TransmissionPart;

@Mapper(componentModel = "spring")
public interface PartDtoMapper {

    @Mapping(source = "type.name", target = "type")
    BasePartResponse toBaseResponse(Part part);

    @Mapping(source = "type.name", target = "type")
    ColorPartResponse toColorResponse(ColorPart part);

    @Mapping(source = "type.name", target = "type")
    TransmissionPartResponse toTransmissionResponse(TransmissionPart part);

    default PartResponse toPolymorphicResponse(Part part) {
        if (part instanceof ColorPart colorPart) {
            return toColorResponse(colorPart);
        } else if (part instanceof TransmissionPart transmissionPart) {
            return toTransmissionResponse(transmissionPart);
        }
        return toBaseResponse(part);
    }
}
