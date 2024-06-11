package app.controller;

import app.controller.dto.AccountStockResponseDto;
import app.controller.dto.AssociateAccountStockDto;
import app.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{accountId}/stocks")
    public ResponseEntity<Void> associateStock(@PathVariable UUID accountId,
                                               @RequestBody AssociateAccountStockDto dto) {

        accountService.associateStock(accountId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountId}/stocks")
    public ResponseEntity<List<AccountStockResponseDto>> listStocks(@PathVariable UUID accountId) {

        var stocks = accountService.listStocks(accountId);
        return ResponseEntity.ok(stocks);
    }
}
