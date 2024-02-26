package com.stablecoinbackend.demo.services;

import com.stablecoinbackend.demo.dto.request.IssuanceApproveRequestDto;
import com.stablecoinbackend.demo.dto.request.IssuanceRejectRequestDto;
import com.stablecoinbackend.demo.dto.request.IssuanceSubmitRequestDto;
import com.stablecoinbackend.demo.dto.response.CashBalanceResponseDto;
import com.stablecoinbackend.demo.dto.response.IssuanceApproveResponseDto;
import com.stablecoinbackend.demo.dto.response.IssuanceRejectResponseDto;
import com.stablecoinbackend.demo.dto.response.WalletBalanceResponseDto;
import com.stablecoinbackend.demo.entities.IssuanceStatus;

import java.io.IOException;
import java.util.List;

public interface FireflyService {
    public void submitIssuanceRequest(IssuanceSubmitRequestDto dto);
    public List<IssuanceStatus> getAllPendingIssuanceRequests();
    public IssuanceApproveResponseDto issueToken(IssuanceApproveRequestDto dto) throws IOException;

    public IssuanceRejectResponseDto rejectToken(IssuanceRejectRequestDto dto);

    public WalletBalanceResponseDto queryWalletBalance(String userId);

    public CashBalanceResponseDto queryCashBalance(String userId);
}
