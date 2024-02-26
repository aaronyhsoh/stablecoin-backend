package com.stablecoinbackend.demo.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssuanceRejectResponseDto {
    private boolean success;
    private String message;

}