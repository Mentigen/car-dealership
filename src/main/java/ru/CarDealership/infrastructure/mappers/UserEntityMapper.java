package ru.CarDealership.infrastructure.mappers;

import org.springframework.stereotype.Component;
import ru.CarDealership.domain.user.User;
import ru.CarDealership.infrastructure.entity.UserEntity;

@Component
public class UserEntityMapper {
    public User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getRole(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getPasswordHash()
        );
    }

    public UserEntity toEntity(User user) {
        var entity = new UserEntity(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getPasswordHash(),
                user.getRole()
        );
        entity.setId(user.getId());
        return entity;
    }
}
