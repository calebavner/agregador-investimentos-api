package app.constants;

import app.controller.dto.CreateUserDto;
import app.model.User;

import java.time.Instant;
import java.util.UUID;

public class UserConstants {

    public static final CreateUserDto CREATE_USER_VALID = new CreateUserDto(
            "username",
            "email@email.com",
            "123"
    );

    public static final CreateUserDto CREATE_USER_INVALID = new CreateUserDto(
            "",
            "email@email.com",
            "123"
    );

    public static final User USER = new User(
            UUID.randomUUID(),
            "username",
            "email@email.com",
            "123",
            Instant.now(),
            null
    );
}
