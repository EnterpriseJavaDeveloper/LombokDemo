package com.herringbone;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.herringbone.exception.StockException;
import com.herringbone.model.Portfolio;
import com.herringbone.model.PortfolioItem;
import com.herringbone.model.Stock;
import com.herringbone.service.PortfolioService;
import lombok.extern.java.Log;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

@Log
public class Main {

    public static void main(String[] args) {
        PortfolioService portfolioService = new PortfolioService();
        try {
            Stock jnj = Stock.builder().symbol("JNJ").date(LocalDateTime.now()).price(175.00).build();
            Stock pg = Stock.builder().symbol("PG").date(LocalDateTime.now()).price(143.00).build();
            PortfolioItem portfolioItem1 = PortfolioItem.builder().numberOfShares(25).purchaseDate(LocalDateTime.of(2022,7, 2, 8, 0)).purchasePrice(165.20).stock(jnj).build();
            PortfolioItem portfolioItem2 = PortfolioItem.builder().numberOfShares(25).purchaseDate(LocalDateTime.of(2022,7, 8, 8, 0)).purchasePrice(145.31).stock(pg).build();

            addStocksConcurrently(portfolioService, portfolioItem1, portfolioItem2);
            addStocksSequentially(portfolioService, portfolioItem1, portfolioItem2);

            Portfolio portfolio = portfolioService.getPortfolio();
            log.log(Level.INFO, portfolio.toString());
        }
        catch (IllegalArgumentException e) {
            log.log(Level.SEVERE, e.getMessage());
            throw new StockException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return;
    }

    private static void addStocksSequentially(PortfolioService portfolioService, PortfolioItem portfolioItem1, PortfolioItem portfolioItem2) {
        portfolioService.addStockToPortfolio(portfolioItem1);
        portfolioService.addStockToPortfolio(portfolioItem2);
        //Try to add null stock
        portfolioService.addStockToPortfolio(null);
    }

    private static void addStocksConcurrently(PortfolioService portfolioService, PortfolioItem portfolioItem1, PortfolioItem portfolioItem2) throws InterruptedException, ExecutionException {
        List<CompletableFuture<Portfolio>> allFutures = new ArrayList<>();
        ExecutorService executorService = Executors.newWorkStealingPool();
        allFutures.add(CompletableFuture.supplyAsync(() -> portfolioService.addStockToPortfolio(portfolioItem1), executorService));
        allFutures.add(CompletableFuture.supplyAsync(() -> portfolioService.addStockToPortfolio(portfolioItem2), executorService));
        //Try to add portfolioItem2 twice
        allFutures.add(CompletableFuture.supplyAsync(() -> portfolioService.addStockToPortfolio(portfolioItem2), executorService));
        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(allFutures.toArray(new CompletableFuture[0]));
        combinedFuture.get();
    }

}
