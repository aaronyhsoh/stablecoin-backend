package com.stablecoinbackend.demo.dto.request;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class RedemptionApproveRequestDto {
    private int id;
    private String pool;
    private BigDecimal amount;
    private String userId;
}
