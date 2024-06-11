package app.service;

import app.controller.dto.CreateUserDto;
import app.exceptions.UserNotFoundException;
import app.model.User;
import app.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UUID createUser(CreateUserDto dto) {
        var user = userRepository.save(new User(null,
                dto.username(),
                dto.email(),
                dto.password(),
                Instant.now(),
                null));

        return user.getUserId();
    }

    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(UUID userId, CreateUserDto dto) {
        return update(userId, dto);
    }

    public void deleteUser(UUID userId) {
        var userExists = userRepository.existsById(userId);

        if(userExists)
            userRepository.deleteById(userId);
    }

    private User update(UUID userId, CreateUserDto dto) {
        var user = userRepository.findById(userId).get();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        userRepository.save(user);

        return user;
    }
}
