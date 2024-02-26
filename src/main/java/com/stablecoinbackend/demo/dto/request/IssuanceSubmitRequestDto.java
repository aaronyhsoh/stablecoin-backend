package com.stablecoinbackend.demo.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class IssuanceSubmitRequestDto {
    private String pool;
    private String currency;
    private BigDecimal amount;
    private String tokenIndex;
    private String messagingMethod;
    private String userId;
}
