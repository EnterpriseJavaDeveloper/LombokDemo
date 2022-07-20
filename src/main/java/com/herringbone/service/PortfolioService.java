package com.herringbone.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.herringbone.exception.StockException;
import com.herringbone.model.Portfolio;
import com.herringbone.model.PortfolioItem;
import com.herringbone.model.Stock;
import com.herringbone.utility.PrintUtility;
import lombok.*;
import lombok.extern.java.Log;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.logging.Level;

@Log
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioService {

    private Portfolio portfolio;
    private ObjectMapper objectMapper = new ObjectMapper();

    @OnStartup
    @SneakyThrows
    private void init() {
        this.portfolio = readPortfolioFromFile();
//        createInitialPortolio();

        this.objectMapper = this.objectMapper.registerModule(new JavaTimeModule());
        String serializedPortfolio = this.objectMapper.writeValueAsString(portfolio);
        log.log(Level.INFO, serializedPortfolio);
    }

    public void createInitialPortolio() {
        Stock aapl = Stock.builder().symbol("AAPL").date(LocalDateTime.now()).price(140.00).build();
        Stock msft = Stock.builder().symbol("MSFT").date(LocalDateTime.now()).price(250.00).build();
        PortfolioItem portfolioItem1 = PortfolioItem.builder().numberOfShares(10).purchaseDate(LocalDateTime.of(2022,2, 2, 8, 0)).purchasePrice(130.20).stock(aapl).build();
        PortfolioItem portfolioItem2 = PortfolioItem.builder().numberOfShares(10).purchaseDate(LocalDateTime.of(2022,2, 8, 8, 0)).purchasePrice(265.31).stock(msft).build();
        this.portfolio = Portfolio.builder().portfolioItem(portfolioItem1).portfolioItem(portfolioItem2).accountType("IRA").brokerage("Fidelity").build();
        log.log(Level.INFO, portfolio.toString());
    }

    @Synchronized
    public Portfolio addStockToPortfolio(@NonNull PortfolioItem portfolioItem) {
        if (portfolio.getPortfolioItems().stream().anyMatch(s -> s.getStock().getSymbol().equals(portfolioItem.getStock().getSymbol()))) {
            throw new StockException("Portfolio already contains " + portfolioItem.getStock().getSymbol());
        }
        try {
            Thread.sleep(2000L); //simulating latency during insert
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.portfolio =  portfolio.toBuilder().portfolioItem(portfolioItem).build();
        return portfolio;
    }

    public Portfolio getPortfolio() {
        return this.portfolio;
    }

    @SneakyThrows
    private Portfolio readPortfolioFromFile() {
        InputStream is = PrintUtility.getFileAsIOStream("Portfolio.json");
        String file = PrintUtility.fileToString(is);
        log.log(Level.INFO, file);

        this.objectMapper.registerModule(new JavaTimeModule());
        Portfolio portfolio = this.objectMapper.readValue(file, Portfolio.class);
        log.log(Level.INFO, portfolio.toString());
        return portfolio;
    }

}
