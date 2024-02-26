package com.stablecoinbackend.demo.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stablecoinbackend.demo.dto.request.IssuanceApproveRequestDto;
import com.stablecoinbackend.demo.dto.request.IssuanceRejectRequestDto;
import com.stablecoinbackend.demo.dto.request.IssuanceSubmitRequestDto;
import com.stablecoinbackend.demo.dto.response.CashBalanceResponseDto;
import com.stablecoinbackend.demo.dto.response.IssuanceApproveResponseDto;
import com.stablecoinbackend.demo.dto.response.IssuanceRejectResponseDto;
import com.stablecoinbackend.demo.dto.response.WalletBalanceResponseDto;
import com.stablecoinbackend.demo.entities.CashBalance;
import com.stablecoinbackend.demo.entities.IssuanceStatus;
import com.stablecoinbackend.demo.entities.WalletBalance;
import com.stablecoinbackend.demo.enums.Enums;
import com.stablecoinbackend.demo.repository.CashBalanceRepository;
import com.stablecoinbackend.demo.repository.IssuanceStatusRepository;
import com.stablecoinbackend.demo.repository.WalletBalanceRepository;
import com.stablecoinbackend.demo.services.FireflyService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class FireflyServiceImpl implements FireflyService {
    @Autowired
    IssuanceStatusRepository issuanceStatusRepository;

    @Autowired
    WalletBalanceRepository walletBalanceRepository;

    @Autowired
    CashBalanceRepository cashBalanceRepository;

    public List<IssuanceStatus> getAllPendingIssuanceRequests() {
        List<IssuanceStatus> pendingList = issuanceStatusRepository.findByApprovalStatus(Enums.ApprovalStatus.PENDING);
        return pendingList;
    }

    public void submitIssuanceRequest(IssuanceSubmitRequestDto dto) {
        IssuanceStatus issuanceStatus = new IssuanceStatus();
        issuanceStatus.setUserId(dto.getUserId());
        issuanceStatus.setPool(dto.getPool());
        issuanceStatus.setCurrency(dto.getCurrency());
        issuanceStatus.setAmount(dto.getAmount());
        issuanceStatus.setApprovalStatus(Enums.ApprovalStatus.PENDING);
        issuanceStatusRepository.save(issuanceStatus);
    }

    public IssuanceRejectResponseDto rejectToken(IssuanceRejectRequestDto dto) {
        IssuanceRejectResponseDto responseDto = new IssuanceRejectResponseDto();
        try {
            IssuanceStatus issuanceStatus = issuanceStatusRepository.findByIdAndApprovalStatus(dto.getIssuanceStatusId(), Enums.ApprovalStatus.PENDING);
            if (issuanceStatus != null) {
                issuanceStatus.setApprovalStatus(Enums.ApprovalStatus.REJECTED);
                issuanceStatusRepository.save(issuanceStatus);
                responseDto.setSuccess(true);
                responseDto.setMessage("");
            } else {
                responseDto.setSuccess(false);
                responseDto.setMessage("Invalid issuance ID");
            }
            return responseDto;
        } catch (Exception e) {
            throw e;
        }
    }

    public IssuanceApproveResponseDto issueToken(IssuanceApproveRequestDto dto) throws IOException {
        try {
            IssuanceStatus issuanceStatus = issuanceStatusRepository.findByIdAndApprovalStatus(dto.getIssuanceStatusId(), Enums.ApprovalStatus.PENDING);
            IssuanceApproveResponseDto responseDto = new IssuanceApproveResponseDto();
            if (issuanceStatus != null) {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                String amountString = Integer.toString(issuanceStatus.getAmount().multiply(BigDecimal.TEN.pow(18)).intValue());
                System.out.println(amountString);
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\"pool\": \"SPDBSC\", \"amount\":\"" + amountString + "\", \"tokenIndex\": \"\", \"messagingMethod\": null}");
                Request request = new Request.Builder()
                        .url("http://127.0.0.1:5309/api/tokens/mint?ns=default")
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        .build();
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(response.body().string(), JsonObject.class);
                    String tokenTransferId = jsonObject.get("id").getAsString();
                    issuanceStatus.setApprovalStatus(Enums.ApprovalStatus.APPROVED);
                    issuanceStatus.setIssuanceId(tokenTransferId);
                    issuanceStatusRepository.save(issuanceStatus);
                    responseDto.setIssuanceId(tokenTransferId);
                    responseDto.setSuccess(true);
                    List<WalletBalance> testingBalance = walletBalanceRepository.findByUserIdAndPool(dto.getUserId(), dto.getPool());

                    WalletBalance coinBalance = walletBalanceRepository.findOneByUserIdAndPool(dto.getUserId(), dto.getPool());
                    if (coinBalance == null) {
                        WalletBalance walletBalance = new WalletBalance();
                        walletBalance.setAmount(issuanceStatus.getAmount());
                        walletBalance.setCurrency(issuanceStatus.getCurrency().trim());
                        walletBalance.setUserId(issuanceStatus.getUserId().trim());
                        walletBalance.setPool(issuanceStatus.getPool().trim());
                        walletBalanceRepository.save(walletBalance);
                    } else {
                        coinBalance.setAmount(coinBalance.getAmount().add(issuanceStatus.getAmount()));
                        walletBalanceRepository.save(coinBalance);
                    }
                } else {
                    responseDto.setSuccess(false);
                    responseDto.setMessage(response.message());
                }
            } else {
                responseDto.setSuccess(false);
                responseDto.setMessage("invalid ID");
            }
            return responseDto;
        } catch (IOException exception) {
            throw exception;
        }
    }

    public WalletBalanceResponseDto queryWalletBalance(String userId) {
        List<WalletBalance> walletBalanceList = walletBalanceRepository.findByUserId(userId);
        WalletBalanceResponseDto responseDto = new WalletBalanceResponseDto();
        responseDto.setWalletBalanceList(walletBalanceList);
        responseDto.setMessage("");
        return responseDto;

    }

    public CashBalanceResponseDto queryCashBalance(String userId) {
        List<CashBalance> cashBalanceList = cashBalanceRepository.findByUserId(userId);
        CashBalanceResponseDto responseDto = new CashBalanceResponseDto();
        responseDto.setCashBalanceList(cashBalanceList);
        responseDto.setMessage("");
        return responseDto;
    }
}
