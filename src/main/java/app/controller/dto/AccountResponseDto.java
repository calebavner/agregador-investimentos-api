package app.controller.dto;

import java.util.UUID;

public record AccountResponseDto(
        UUID accountId,
        String description
) {
}
