package app.service;

import app.clients.BrapiClient;
import app.controller.dto.AccountStockResponseDto;
import app.controller.dto.AssociateAccountStockDto;
import app.model.AccountStock;
import app.model.AccountStockId;
import app.repository.AccountRepository;
import app.repository.AccountStockRepository;
import app.repository.StockRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    @Value("#{environment.TOKEN}")
    private String TOKEN;

    private final AccountRepository accountRepository;
    private final StockRepository stockRepository;
    private final AccountStockRepository accountStockRepository;
    private final BrapiClient brapiClient;

    public AccountService(AccountRepository accountRepository, StockRepository stockRepository, AccountStockRepository accountStockRepository, BrapiClient brapiClient) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
        this.brapiClient = brapiClient;
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
                .map(obj -> new AccountStockResponseDto(obj.getStock().getStockId(),
                        obj.getQuantity(),
                        getTotal(obj.getQuantity(), obj.getStock().getStockId())))
                .toList();
    }

    private Double getTotal(Integer quantity, String stockId) {
        var response = brapiClient.getQuote(TOKEN, stockId);
        var price = response.results().get(0).regularMarketPrice();

        return quantity * price;
    }
}
