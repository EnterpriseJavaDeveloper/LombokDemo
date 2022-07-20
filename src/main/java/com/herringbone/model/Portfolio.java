package com.herringbone.model;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class Portfolio {
    @Singular
    private List<PortfolioItem> portfolioItems;
    private String accountType;
    private String brokerage;

}
