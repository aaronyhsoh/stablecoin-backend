package com.stablecoinbackend.demo.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stablecoinbackend.demo.dto.request.*;
import com.stablecoinbackend.demo.dto.response.*;
import com.stablecoinbackend.demo.entities.*;
import com.stablecoinbackend.demo.enums.Enums;
import com.stablecoinbackend.demo.repository.*;
import com.stablecoinbackend.demo.services.FireflyService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class FireflyServiceImpl implements FireflyService {
    @Autowired
    IssuanceStatusRepository issuanceStatusRepository;

    @Autowired
    WalletBalanceRepository walletBalanceRepository;

    @Autowired
    CashBalanceRepository cashBalanceRepository;

    @Autowired
    RedemptionStatusRepository redemptionStatusRepository;

    @Autowired
    TransferLogRepository transferLogRepository;

    public List<IssuanceStatus> getAllIssuanceRequestsByUserId(String userId) {
        List<IssuanceStatus> pendingList = issuanceStatusRepository.findByUserId(userId);
        return pendingList;
    }

    public List<IssuanceStatus> getAllIssuanceRequests() {
        List<IssuanceStatus> pendingList = issuanceStatusRepository.findAll();
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
                String amountString = issuanceStatus.getAmount().multiply(BigDecimal.TEN.pow(18)).toBigInteger().toString();
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
                    //List<WalletBalance> testingBalance = walletBalanceRepository.findByUserIdAndPool(dto.getUserId(), dto.getPool());

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

    public RedemptionSubmitResponseDto submitRedemptionRequest(RedemptionSubmitRequestDto dto) {
        WalletBalance walletBalance = walletBalanceRepository.findByUserIdAndPool(dto.getUserId(), dto.getPool());
        RedemptionSubmitResponseDto responseDto = new RedemptionSubmitResponseDto();
        if (walletBalance != null) {
            if (walletBalance.getAmount().subtract(dto.getAmount()).compareTo(BigDecimal.ZERO) == 1) {
                RedemptionStatus redemptionStatus = new RedemptionStatus();
                redemptionStatus.setAmount(dto.getAmount());
                redemptionStatus.setPool(dto.getPool());
                redemptionStatus.setUserId(dto.getUserId());
                redemptionStatus.setApprovalStatus(Enums.ApprovalStatus.PENDING);
                redemptionStatusRepository.save(redemptionStatus);
                responseDto.setSuccess(true);
                responseDto.setMessage("");
            } else {
                responseDto.setSuccess(false);
                responseDto.setMessage("Insufficient amount to redeem");
            }
        } else {
            responseDto.setSuccess(false);
            responseDto.setMessage("Invalid request");
        }

        return responseDto;
    }

    public void transferToken(TransferTokenRequestDto dto) {
        WalletBalance senderBalance = walletBalanceRepository.findOneByUserIdAndPool(dto.getFrom(), dto.getPool());
        WalletBalance receiverBalance = walletBalanceRepository.findOneByUserIdAndPool(dto.getTo(), dto.getPool());
        if (senderBalance != null && (senderBalance.getAmount().compareTo(dto.getAmount()) >= 0)) {
            // call firefly transfer API


            TransferLog transferLog = new TransferLog();
            transferLog.setAmount(dto.getAmount());
            transferLog.setPool(dto.getPool());
            transferLog.setCurrency(dto.getCurrency());
            transferLog.setUserId(dto.getFrom());
            transferLogRepository.save(transferLog);

            senderBalance.setAmount(senderBalance.getAmount().subtract(dto.getAmount()));
            walletBalanceRepository.save(senderBalance);

            if (receiverBalance == null) {
                receiverBalance = new WalletBalance();
                receiverBalance.setPool(dto.getPool());
                receiverBalance.setCurrency(dto.getCurrency());
                receiverBalance.setAmount(dto.getAmount());
                receiverBalance.setUserId(dto.getTo());
            } else {
                receiverBalance.setAmount(receiverBalance.getAmount().add(dto.getAmount()));
            }
            walletBalanceRepository.save(receiverBalance);

        }

    }
}
