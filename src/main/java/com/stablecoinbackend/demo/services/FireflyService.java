package com.stablecoinbackend.demo.services;

import com.stablecoinbackend.demo.dto.request.*;
import com.stablecoinbackend.demo.dto.response.*;
import com.stablecoinbackend.demo.entities.IssuanceStatus;
import com.stablecoinbackend.demo.entities.RedemptionStatus;

import java.io.IOException;
import java.util.List;

public interface FireflyService {
    public void submitIssuanceRequest(IssuanceSubmitRequestDto dto);
    public List<IssuanceStatus> getAllIssuanceRequestsByUserId(String userId);

    public List<IssuanceStatus> getAllIssuanceRequests();
    public IssuanceApproveResponseDto issueToken(IssuanceApproveRequestDto dto) throws IOException;

    public IssuanceRejectResponseDto rejectToken(IssuanceRejectRequestDto dto);

    public WalletBalanceResponseDto queryWalletBalance(String userId);

    public CashBalanceResponseDto queryCashBalance(String userId);

    public RedemptionSubmitResponseDto submitRedemptionRequest(RedemptionSubmitRequestDto dto);

    public TransferTokenResponseDto transferToken(TransferTokenRequestDto dto) throws IOException;

    public List<RedemptionStatus> queryAllRedemptionStatus();

    public RedemptionApproveResponseDto approveRedemption(RedemptionApproveRequestDto dto) throws IOException;
}
