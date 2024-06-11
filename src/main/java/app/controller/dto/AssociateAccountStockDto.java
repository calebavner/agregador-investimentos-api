package app.controller.dto;

import java.util.UUID;

public record AssociateAccountStockDto(
        String stockId,
        Integer quantity
) {
}
