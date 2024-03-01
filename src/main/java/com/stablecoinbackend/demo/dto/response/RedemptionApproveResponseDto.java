package com.stablecoinbackend.demo.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RedemptionApproveResponseDto {
    private String burnId;
    private String msg;
    private boolean success;
}
