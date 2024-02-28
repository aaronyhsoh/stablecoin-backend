package com.stablecoinbackend.demo.dto.request;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TransferTokenRequestDto {
    private String from;
    private String to;
    private String pool;
    private String currency;
    private BigDecimal amount;
}
