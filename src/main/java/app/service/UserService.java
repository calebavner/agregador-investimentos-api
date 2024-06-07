package app.service;

import app.controller.dto.CreateUserDto;
import app.model.User;
import app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID createUSer(CreateUserDto dto) {
        var user = userRepository.save(new User(null,
                dto.username(),
                dto.email(),
                dto.password(),
                Instant.now(),
                null));

        return user.getUserId();
    }
}
