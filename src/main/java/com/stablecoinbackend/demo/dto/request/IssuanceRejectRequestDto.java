package com.stablecoinbackend.demo.dto.request;

import lombok.Getter;

@Getter
public class IssuanceRejectRequestDto {
    private String userId;
    private int issuanceStatusId;
}
