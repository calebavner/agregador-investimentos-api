package app.clients.dto;

import java.util.List;

public record BrapiResponseDto(
        List<StockDto> results
) {
}
