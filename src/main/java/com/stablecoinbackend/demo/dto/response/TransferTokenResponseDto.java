package com.stablecoinbackend.demo.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferTokenResponseDto {
    private String transferId;
    private boolean success;
    private String message;
}
