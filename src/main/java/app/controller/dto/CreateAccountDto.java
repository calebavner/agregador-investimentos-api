package app.controller.dto;

public record CreateAccountDto(
        String description,
        String street,
        Integer number
) {
}
