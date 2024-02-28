package com.stablecoinbackend.demo.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class RedemptionSubmitRequestDto {
    private String pool;
    private BigDecimal amount;
    private String userId;
}
