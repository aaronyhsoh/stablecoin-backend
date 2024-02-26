package com.stablecoinbackend.demo.dto.response;

import com.stablecoinbackend.demo.entities.CashBalance;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CashBalanceResponseDto {
    private String message;
    private List<CashBalance> cashBalanceList;
}
