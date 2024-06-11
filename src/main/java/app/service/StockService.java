package app.service;

import app.controller.dto.CreateStockDto;
import app.model.Stock;
import app.repository.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void createStock(CreateStockDto dto) {
        var stock = new Stock(
                dto.stockId(),
                dto.description()
        );

        stockRepository.save(stock);
    }
}
