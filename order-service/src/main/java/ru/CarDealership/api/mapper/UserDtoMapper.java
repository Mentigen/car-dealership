package ru.CarDealership.api.mapper;

import org.mapstruct.Mapper;
import ru.CarDealership.api.dto.UserResponse;
import ru.CarDealership.domain.user.User;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    UserResponse toResponse(User user);
}
