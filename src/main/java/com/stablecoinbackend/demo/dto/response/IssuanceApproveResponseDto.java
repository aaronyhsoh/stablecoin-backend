package com.stablecoinbackend.demo.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssuanceApproveResponseDto {
    private String issuanceId;
    private boolean success;
    private String message;

}
