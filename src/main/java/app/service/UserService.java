package app.service;

import app.controller.dto.AccountResponseDto;
import app.controller.dto.CreateAccountDto;
import app.controller.dto.CreateUserDto;
import app.exceptions.UserNotFoundException;
import app.model.Account;
import app.model.BillingAddress;
import app.model.User;
import app.repository.AccountRepository;
import app.repository.BillingAddressRepository;
import app.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final BillingAddressRepository billingAddressRepository;

    public UserService(UserRepository userRepository, AccountRepository accountRepository, BillingAddressRepository billingAddressRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.billingAddressRepository = billingAddressRepository;
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

    public void createAccount(UUID userId, CreateAccountDto dto) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var account = new Account(
            UUID.randomUUID(),
            dto.description(),
            user,
            null,
            new ArrayList<>()
        );

        var accountCreated = accountRepository.save(account);

        var billingAddress = new BillingAddress(
                accountCreated.getAccountId(),
                accountCreated,
                dto.street(),
                dto.number()
        );

        billingAddressRepository.save(billingAddress);
    }

    public List<AccountResponseDto> listAllAccounts(UUID userId) {

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var response = user.getAccounts()
                                .stream()
                                .map(account -> new AccountResponseDto(account.getAccountId(), account.getDescription()))
                                .toList();

        return response;
    }
}
