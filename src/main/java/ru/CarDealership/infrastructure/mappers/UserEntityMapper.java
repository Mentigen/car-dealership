package ru.CarDealership.infrastructure.mappers;

import lombok.extern.apachecommons.CommonsLog;
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
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setRole(user.getRole());
        entity.setEmail(user.getEmail());
        entity.setPhone(user.getPhone());
        entity.setPasswordHash(user.getPasswordHash());
        return entity;
    }
}
