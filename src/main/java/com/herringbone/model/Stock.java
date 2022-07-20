package com.herringbone.model;

import lombok.Builder;
import lombok.Data;
import lombok.OnStartup;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Data
@SuperBuilder(toBuilder = true)
@Jacksonized
public class Stock {
    private String symbol;
    private Double price;
    @Builder.Default
    private LocalDateTime date  = LocalDateTime.now();

    @OnStartup
    public static void startup() {

    }
}
