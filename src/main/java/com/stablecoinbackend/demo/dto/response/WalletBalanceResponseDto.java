package com.stablecoinbackend.demo.dto.response;

import com.stablecoinbackend.demo.entities.WalletBalance;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WalletBalanceResponseDto {
    private String message;
    private List<WalletBalance> walletBalanceList;
}
