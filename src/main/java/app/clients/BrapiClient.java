package app.clients;

import app.clients.dto.BrapiResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "BrapiClient",
    url = "https://brapi.dev"
)
public interface BrapiClient {

    @GetMapping("/api/quote/{stockId}")
    BrapiResponseDto getQuote(@RequestParam String token,
                              @PathVariable String stockId);

}
