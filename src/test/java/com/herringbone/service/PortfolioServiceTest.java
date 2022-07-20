package com.herringbone.service;

import com.herringbone.model.PortfolioItem;
import com.herringbone.model.Stock;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class PortfolioServiceTest {

    @Test
    void addStock() {
        PortfolioService portfolioService = new PortfolioService();
        PortfolioItem portfolioItem = PortfolioItem.builder().numberOfShares(10)
                .stock(Stock.builder().symbol("AAPL").price(135.20).build())
                .purchasePrice(130.34)
                .purchaseDate(LocalDateTime.now()).build();
        portfolioService.addStockToPortfolio(portfolioItem);
    }
}