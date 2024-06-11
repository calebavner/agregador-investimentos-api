package app.controller.dto;

public record AccountStockResponseDto(
        String stockId,
        Integer quantity,
        Double total
) {
}
