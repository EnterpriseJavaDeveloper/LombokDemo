package com.herringbone.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class DetailStock extends Stock {
    private Long volume;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
}
