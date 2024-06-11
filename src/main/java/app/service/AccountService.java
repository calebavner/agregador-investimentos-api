package app.service;

import app.controller.dto.AccountResponseDto;
import app.controller.dto.AccountStockResponseDto;
import app.controller.dto.AssociateAccountStockDto;
import app.model.AccountStock;
import app.model.AccountStockId;
import app.repository.AccountRepository;
import app.repository.AccountStockRepository;
import app.repository.StockRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final StockRepository stockRepository;
    private final AccountStockRepository accountStockRepository;

    public AccountService(AccountRepository accountRepository, StockRepository stockRepository, AccountStockRepository accountStockRepository) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
    }

    public void associateStock(UUID accountId, AssociateAccountStockDto dto) {

        var account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var stock = stockRepository.findById(dto.stockId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var id = new AccountStockId(account.getAccountId(), stock.getStockId());
        var obj = new AccountStock(
                id,
                account,
                stock,
                dto.quantity()
        );

        accountStockRepository.save(obj);
    }

    public List<AccountStockResponseDto> listStocks(UUID accountId) {

        var account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return account.getAccountStocks()
                .stream()
                .map(obj -> new AccountStockResponseDto(obj.getStock().getStockId(), obj.getQuantity(), 0D))
                .toList();
    }
}
