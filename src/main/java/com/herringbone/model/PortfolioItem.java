package com.herringbone.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Data
@Builder
@Jacksonized
public class PortfolioItem {
    private Stock stock;
    private Integer numberOfShares;
    private LocalDateTime purchaseDate;
    private Double purchasePrice;
}
