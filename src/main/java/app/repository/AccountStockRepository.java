package app.repository;

import app.model.AccountStock;
import app.model.AccountStockId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountStockRepository extends JpaRepository<AccountStock, AccountStockId> {
}
